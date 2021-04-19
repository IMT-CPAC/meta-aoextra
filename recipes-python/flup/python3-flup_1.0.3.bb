LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING;md5=d50460c0b843f8fc95fbc28d58cb9af2"

SRC_URI += " \
	file://COPYING	\
"

SRC_URI[md5sum] = "a64e7a6374e043480ee92534c735964e"
SRC_URI[sha256sum] = "5eb09f26eb0751f8380d8ac43d1dfb20e1d42eca0fa45ea9289fa532a79cd159"

require python-flup.inc

inherit setuptools3
