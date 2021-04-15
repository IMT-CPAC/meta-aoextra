DESCRIPTION = "wxWidgets is a cross platform application framework utilizing native python."
HOMEPAGE = "http://www.wxpython.org"

LICENSE = "WXwindows"
LIC_FILES_CHKSUM = "file://wxPython/licence/licence.txt;md5=18346072db6eb834b6edbd2cdc4f109b"

DEPENDS = "wxwidgets"

SRC_URI = "${SOURCEFORGE_MIRROR}/wxpython/wxPython-src-${PV}.tar.bz2"
SRC_URI[md5sum] = "922b02ff2c0202a7bf1607c98bbbbc04"
SRC_URI[sha256sum] = "d54129e5fbea4fb8091c87b2980760b72c22a386cb3b9dd2eebc928ef5e8df61"

# S = "${WORKDIR}/wxPython-src-${PV}/wxPython"
S = "${WORKDIR}/wxPython-src-${PV}"
B = "${S}/wxPython"

inherit pkgconfig pythonnative python-dir distutils

CFLAGS += "-std=gnu++11"
CFLAGS += "-I../include -I../src"
CFLAGS += "`wx-config --cppflags`"

# Enable output on stdout for buildpaths
export WXDEBUG = "findprogress"

EXTRA_OECONF_append_raspberrypi3 = " --enable-compat28"

# remove -L/usr/X11R6/lib hardcodes
do_configure_prepend() {
	wx-config --prefix
	sed -i -e s:/usr/X11R6/lib::g ${B}/config.py
}

# G. Oliver <go@aerodesic.com> misplled? (was "do_iinstall_append")
# do_install_append() {
#    cp -a ${D}${STAGING_DIR_HOST}/* ${D}
#    rm -rf ${D}${STAGING_DIR}	
# }
