DESCRIPTION = "LabJack LJM libraries"
SECTION = "devel/python"
PRIORITY = "optional"
LICENSE = "MIT-X"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCNAME = "labjack-ljm"

inherit pypi

SRC_URI = "\
	http://pypi.python.org/packages/source/l/${SRCNAME}/${SRCNAME}-${PV}.tar.gz	\
"

RDEPENDS_${PN} = "\
	labjack-lib	\
"

S = "${WORKDIR}/${SRCNAME}-${PV}"

