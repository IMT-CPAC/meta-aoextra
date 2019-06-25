
SUMMARY = "ISO country, subdivision, language, currency and script definitions and their translations"
HOMEPAGE = ""
AUTHOR = "Christian Theune <ct@flyingcircus.io>"
LICENSE = "LGPL2.1"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2e3f24d82bf0f89a5a3be6801b999bf1"

SRC_URI = "https://files.pythonhosted.org/packages/87/c7/c2c76c3ae4ac79c74c1871ae775ed97b70d475dd90d1e824b1d2fc0cd54f/pycountry-18.12.8.tar.gz"
SRC_URI[md5sum] = "46223fa49c45c304083de7d5b1870fb7"
SRC_URI[sha256sum] = "8ec4020b2b15cd410893d573820d42ee12fe50365332e58c0975c953b60a16de"

S = "${WORKDIR}/pycountry-18.12.8"

RDEPENDS_${PN} = ""

inherit setuptools
