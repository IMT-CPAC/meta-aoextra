DESCRIPTION = "Python dependency based reloader"
HOMEPAGE = "https://github.com/jparise/python-reloader"
AUTHOR = "jon@indelible.org"
SECTION = "devel/python"
PRIORITY = "optional"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCNAME = "reloader"
PR = "r0"

SRC_URI = "\
	http://pypi.python.org/packages/source/r/${SRCNAME}/${SRCNAME}-${PV}.tar.gz	\
"

S = "${WORKDIR}/${SRCNAME}-${PV}"

SRC_URI[md5sum] = "6709441b2dd9a4d22ef13b21d17a72fa"
SRC_URI[sha256sum] = "2bca19c2450bcac75110ae2b0d8c5f7bb2c11dd05e163fb8e121f6c1130fd922"

