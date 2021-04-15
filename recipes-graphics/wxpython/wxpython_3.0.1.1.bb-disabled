DESCRIPTION = "wxWidgets is a cross platform application framework utilizing native python."
HOMEPAGE = "http://www.wxpython.org"

LICENSE = "WXwindows"
LIC_FILES_CHKSUM = "file://wxPython/licence/licence.txt;md5=18346072db6eb834b6edbd2cdc4f109b"

DEPENDS = "wxwidgets"

SRC_URI = "${SOURCEFORGE_MIRROR}/wxpython/wxPython-src-${PV}.tar.bz2"
SRC_URI[md5sum] = "f4a4c228a2256c5804ec4996aa683b89"
SRC_URI[sha256sum] = "d51463fe760e2fc00271678700c216809f9db32a6081a1808bd4b7c884dc0a32"

S = "${WORKDIR}/wxPython-src-${PV}"
B = "${S}/wxPython"

inherit pkgconfig pythonnative python-dir distutils

CFLAGS += "-std=gnu++11"
CFLAGS += "-I../include -I../src"
CFLAGS += "`wx-config --cppflags`"

# Enable output on stdout for buildpaths
export WXDEBUG = "findprogress"

EXTRA_OECONF_append_raspberrypi3 = "--enable-compat28"

# remove -L/usr/X11R6/lib hardcodes
do_configure_prepend() {
	sed -i -e s:/usr/X11R6/lib::g ${S}/config.py
}

# G. Oliver <go@aerodesic.com> misplled? (was "do_iinstall_append")
do_install_append() {
    cp -a ${D}${STAGING_DIR_HOST}/* ${D}
    rm -rf ${D}${STAGING_DIR}	
}
