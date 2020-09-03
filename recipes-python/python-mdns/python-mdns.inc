DESCRIPTION = "python MDNS interface for AVAHI"
HOMEPAGE = "https://github.com/carlosefr/mdns-publisher"
SECTION = "devel/python"
PRIORITY = "optional"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCNAME = "mdns-publisher"
PR = "r0"

SRC_URI = "\
	http://pypi.python.org/packages/source/m/${SRCNAME}/${SRCNAME}-${PV}.tar.gz	\
"

S = "${WORKDIR}/${SRCNAME}-${PV}"

SRC_URI[md5sum] = "bcd7601229f057eefbf4bb4afd24b4f4"
SRC_URI[sha256sum] = "b230902a7b0547ac372300af675183c3456a9cb8c46cedde72028e26b4998fce"

