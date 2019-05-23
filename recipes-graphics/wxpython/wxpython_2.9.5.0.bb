HOMEPAGE = "http://www.wxpython.org"

LICENSE = "WXwindows"
LIC_FILES_CHKSUM = "file://wxPython/licence/licence.txt;md5=18346072db6eb834b6edbd2cdc4f109b"

DEPENDS = "wxwidgets gstreamer"

SRC_URI = "${SOURCEFORGE_MIRROR}/wxpython/wxPython-src-${PV}.tar.bz2"
SRC_URI[md5sum] = "a2a28fe8223391c93bf8788316767c9e"
SRC_URI[sha256sum] = "1a5b7e771eff467538d0834136188e8a7506a4fe6e85d0a46c40158cdbd4c48c"

S = "${WORKDIR}/wxPython-src-${PV}"
B = "${S}/wxPython"

inherit pkgconfig pythonnative python-dir distutils

EXTRA_OECONF_append_raspberrypi3 = "\
	--enable-debug_info \
	--enable-compat28 \
	--with-gtk \
	--with-opengl \
	--disable-mediactrl \
	--disable-html \
	--disable-webviewwebkit \
	--disable-webkit \
	--disable-webview \
    "

CFLAGS += "-std=gnu++11"
CFLAGS += "-I../include -I../src"
CFLAGS += "`wx-config --cppflags`"

# Enable output on stdout for buildpaths
export WXDEBUG = "findprogress"

# remove -L/usr/X11R6/lib hardcodes
do_configure_prepend() {
	sed -i -e s:/usr/X11R6/lib::g ${S}/config.py
}

# e.g. ${D}/build/v2013.06/build/tmp-angstrom_v2013_06-eglibc/sysroots/beaglebone/usr/include/wx-2.9/wx/wxPython/pytree.h

# G. Oliver <go@aerodesic.com> misplled?
# do_iinstall_append() {
do_install_append() {
    cp -a ${D}${STAGING_DIR_HOST}/* ${D}
    rm -rf ${D}${STAGING_DIR}	
}
