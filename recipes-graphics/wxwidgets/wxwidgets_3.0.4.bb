DESCRIPTION = "wxWidgets is a cross platform application framework utilizing native widgets."
HOMEPAGE = "http://www.wxwidgets.org"
SECTION = "libs"

LICENSE = "WXwindows"
LIC_FILES_CHKSUM = "file://docs/licence.txt;md5=18346072db6eb834b6edbd2cdc4f109b"

# DEPENDS = "webkitgtk gstreamer gtk+ jpeg tiff libpng zlib expat libxinerama libglu"
DEPENDS = "gtk+ jpeg tiff libpng zlib expat libxinerama libglu"

SRC_URI = "\
	https://github.com/wxWidgets/wxWidgets/releases/download/v${PV}/wxWidgets-${PV}.tar.bz2 \
	file://0001-Modified-makefile-to-produce-correct-symbolic-link-f.patch \
   "

SRC_URI[md5sum] = "b0035731777acc5597cea8982da10317"
SRC_URI[sha256sum] = "96157f988d261b7368e5340afa1a0cad943768f35929c22841f62c25b17bf7f0"

S = "${WORKDIR}/wxWidgets-${PV}"

inherit autotools-brokensep pkgconfig binconfig

EXTRA_AUTORECONF = " -I ${S}/build/aclocal"
EXTRA_OECONF = " \
	--enable-compat28=yes \
        --enable-unicode \
	--with-opengl \
	--without-sdl \
	--disable-gpe \
	--disable-visibility \
	--disable-rpath \
	--disable-mediactrl \
	--disable-webviewwebkit \
    "

# --enable-mediactrl=yes
# --enable-webviewwebkit=yes

# CXXFLAGS := "${@oe_filter_out('-fvisibility-inlines-hidden', '${CXXFLAGS}', d)}"
CXXFLAGS_remove = "-fvisibility-inlines-hidden"
CXXFLAGS += "-std=gnu++11"

# Broken autotools :/
do_configure() {
	oe_runconf
}

# wx-config contains entries like this:
# this_prefix=`check_dirname "/build/v2013.06/build/tmp-angstrom_v2013_06-eglibc/work/cortexa8hf-vfp-neon-angstrom-linux-gnueabi/wxwidgets/2.9.5-r0/wxWidgets-2.9.5"`
do_install_prepend() {
	sed -i -e s:${S}:${COMPONENTS_DIR}/${PACKAGE_ARCH}/${PN}${prefix}:g ${S}/wx-config
	# sed -i -e s:${S}:${STAGING_DIR_HOST}${prefix}:g ${S}/wx-config
}


# wx-config doesn't handle the suffixed libwx_media, xrc, etc, make a compat symlink
do_install_append() {
	echo TARGET_SYS=${TARGET_SYS}
	for lib in adv aui core html media propgrid qa ribbon richtext stc webview xrc ; do
		if [ -e ${D}${libdir}/libwx_gtk2u_$lib-3.0-${TARGET_SYS}.so ]; then
			ln -sf libwx_gtk2u_$lib-3.0-${TARGET_SYS}.so ${D}${libdir}/libwx_gtk2u_$lib-3.0.so
		fi
	done
}

SYSROOT_PREPROCESS_FUNCS += "wxwidgets_sysroot_preprocess"
wxwidgets_sysroot_preprocess () {
	# G. Oliver <go@aerodesic.com> Correct the include path
	sed -i -e 's:includedir="/usr/include":includedir="${COMPONENTS_DIR}/${PACKAGE_ARCH}/${PN}${prefix}/include/wx-3.0":g' ${SYSROOT_DESTDIR}${libdir}/wx/config/*
	sed -i -e 's:libdir="/usr/lib":libdir="${COMPONENTS_DIR}/${PACKAGE_ARCH}/${PN}${libdir}":g'                            ${SYSROOT_DESTDIR}${libdir}/wx/config/*
}

FILES_${PN} +=     "${bindir} ${libdir}/wx/config"
FILES_${PN}-dev += "${libdir}/wx/include ${datadir}/bakefile"
