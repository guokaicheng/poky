SUMMARY = "Support for reading various archive formats"
DESCRIPTION = "C library and command-line tools for reading and writing tar, cpio, zip, ISO, and other archive formats"
HOMEPAGE = "http://www.libarchive.org/"
SECTION = "devel"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=ed99aca006bc346974bb745a35336425"

DEPENDS = "e2fsprogs-native"

PACKAGECONFIG ?= "zlib bz2"

PACKAGECONFIG_append_class-target = "\
	libxml2 \
	${@bb.utils.contains('DISTRO_FEATURES', 'acl', 'acl', '', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'xattr', 'xattr', '', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'largefile', 'largefile', '', d)} \
"

PACKAGECONFIG_append_class-nativesdk = " largefile"

PACKAGECONFIG[acl] = "--enable-acl,--disable-acl,acl,"
PACKAGECONFIG[xattr] = "--enable-xattr,--disable-xattr,attr,"
PACKAGECONFIG[largefile] = "--enable-largefile,--disable-largefile,,"
PACKAGECONFIG[zlib] = "--with-zlib,--without-zlib,zlib,"
PACKAGECONFIG[bz2] = "--with-bz2lib,--without-bz2lib,bzip2,"
PACKAGECONFIG[xz] = "--with-lzmadec --with-lzma,--without-lzmadec --without-lzma,xz,"
PACKAGECONFIG[openssl] = "--with-openssl,--without-openssl,openssl,"
PACKAGECONFIG[libxml2] = "--with-xml2,--without-xml2,libxml2,"
PACKAGECONFIG[expat] = "--with-expat,--without-expat,expat,"
PACKAGECONFIG[lzo] = "--with-lzo2,--without-lzo2,lzo,"
PACKAGECONFIG[nettle] = "--with-nettle,--without-nettle,nettle,"
PACKAGECONFIG[lz4] = "--with-lz4,--without-lz4,lz4,"

SRC_URI = "http://libarchive.org/downloads/libarchive-${PV}.tar.gz \
           file://0001-configure.ac-check-acl-libacl.h-and-sys-acl.h-based-.patch \
           "

SRC_URI[md5sum] = "596210b5a9c2ff74a0f7ca34838d655f"
SRC_URI[sha256sum] = "7bce45fd71ff01dc20d19edd78322d4965583d81b8bed8e26cacb65d6f5baa87"

inherit autotools update-alternatives pkgconfig

CPPFLAGS += "-I${WORKDIR}/extra-includes"

do_configure[cleandirs] += "${WORKDIR}/extra-includes"
do_configure_prepend() {
	# We just need the headers for some type constants, so no need to
	# build all of e2fsprogs for the target
	cp -R ${STAGING_INCDIR_NATIVE}/ext2fs ${WORKDIR}/extra-includes/
}

ALTERNATIVE_PRIORITY = "100"

PACKAGES =+ "bsdtar"
FILES_bsdtar = "${bindir}/bsdtar"

ALTERNATIVE_bsdtar = "tar"
ALTERNATIVE_LINK_NAME[tar] = "${base_bindir}/tar"
ALTERNATIVE_TARGET[tar] = "${bindir}/bsdtar"

PACKAGES =+ "bsdcpio"
FILES_bsdcpio = "${bindir}/bsdcpio"

ALTERNATIVE_bsdcpio = "cpio"
ALTERNATIVE_LINK_NAME[cpio] = "${base_bindir}/cpio"
ALTERNATIVE_TARGET[cpio] = "${bindir}/bsdcpio"

BBCLASSEXTEND = "native nativesdk"
