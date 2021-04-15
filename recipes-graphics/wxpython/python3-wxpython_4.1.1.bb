DESCRIPTION = "wxPython interface to wxwidgets"
LICENSE = "WXwindows"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=fce1d18e2d633d41786c0a8dfbc80917"

PYPI_PACKAGE = "wxPython"

DEPENDS = "\
	wxwidgets		\
	gtk+3			\
	"

RDEPENDS_${PN} = "\
	wxwidgets		\
	gtk+3			\
	"

inherit pypi setuptools3 distutils3


SRC_URI[md5sum] = "262191ae1c926a58da37fb7a8fabc51e"
SRC_URI[sha256sum] = "00e5e3180ac7f2852f342ad341d57c44e7e4326de0b550b9a5c4a8361b6c3528"


# Add patch file(s)
SRC_URI += "\
	file://9000-config-patch-for-version-3.patch	\
	"

S = "${WORKDIR}/wxPython-${PV}"

# The 'build' parameter isn't getting passed correctly..  But fortunately we don't need it.
# CROSS_COMPILE_ARGS="--host=${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS} --build=${BUILD_ARCH}"
CROSS_COMPILE_ARGS="--host=${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS}"

do_compile() {
    NO_FETCH_BUILD=1 \
    STAGING_INCDIR=${STAGING_INCDIR} \
    STAGING_LIBDIR=${STAGING_LIBDIR} \
    ${STAGING_BINDIR_NATIVE}/${PYTHON_PN}-native/${PYTHON_PN} setup.py \
    build ${DISTUTILS_BUILD_ARGS} ${CROSS_COMPILE_ARGS} || \
    bbfatal_log "'${PYTHON_PN} setup.py build ${DISTUTILS_BUILD_ARGS}' execution failed."
}

BBCLASSEXTEND = "native nativesdk"

do_install_append() {
    # Clean up unneeded files
    rm -rf `find ${D}${PYTHON_SITEPACKAGES_DIR} -type d -name __pycache__`
}
      
SO_LIBS = ".so.*"
SO_LIBS_DEV = ".so"
FILES_SO_LIBS_DEV = "${PYTHON_SITEPACKAGES_DIR}/wx/libwx*${SO_LIBS_DEV}"

FILES_${PN}-dev += "${bindir} ${FILES_SO_LIBS_DEV}"

