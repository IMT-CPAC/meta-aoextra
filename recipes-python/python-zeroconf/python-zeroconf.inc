DESCRIPTION = "ZEROCONF client for python"
HOMEPAGE = "https://github.com/jstasiak/python-zeroconf"
SECTION = "devel/python"
PRIORITY = "optional"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=27818cd7fd83877a8e3ef82b82798ef4"

SRCNAME = "zeroconf"
PR = "r0"

SRC_URI = "\
	http://pypi.python.org/packages/source/z/${SRCNAME}/${SRCNAME}-${PV}.tar.gz	\
"

S = "${WORKDIR}/${SRCNAME}-${PV}"

SRC_URI[md5sum] = "fc22fe1a269edfa15abcae32c022a560"
SRC_URI[sha256sum] = "902e6c3ca4cc752577d650d05a3e7102a897b647fe76da7c0d322cd493cbd1a3"

