DESCRIPTION = "wxPython interface to wxwidgets"
LICENSE = "WXwindows"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=fce1d18e2d633d41786c0a8dfbc80917"

PYPI_PACKAGE = "wxPython"

DEPENDS = "\
    wxwidgets                   \
    gtk+3                       \
"

RDEPENDS_${PN} = "\
    wxwidgets                   \
    gtk+3                       \
"

inherit pypi setuptools3 distutils3


SRC_URI[md5sum] = "262191ae1c926a58da37fb7a8fabc51e"
SRC_URI[sha256sum] = "00e5e3180ac7f2852f342ad341d57c44e7e4326de0b550b9a5c4a8361b6c3528"


# Add patch file(s)
SRC_URI += "\
    file://9000-config-patch-for-host-build-parameter-passthrough.patch		\
"

S = "${WORKDIR}/wxPython-${PV}"

# The 'build' parameter isn't getting passed correctly..  But fortunately we don't need it.
# CROSS_COMPILE_ARGS="--host=${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS} --build=${BUILD_ARCH}"
CROSS_COMPILE_ARGS="--host=${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS}"

do_compile() {
    cd ${S} \
    NO_FETCH_BUILD=1 \
    STAGING_INCDIR=${STAGING_INCDIR} \
    STAGING_LIBDIR=${STAGING_LIBDIR} \
    ${STAGING_BINDIR_NATIVE}/${PYTHON_PN}-native/${PYTHON_PN} setup.py \
    build ${DISTUTILS_BUILD_ARGS} ${CROSS_COMPILE_ARGS} || \
    bbfatal_log "'${PYTHON_PN} setup.py build ${DISTUTILS_BUILD_ARGS}' execution failed."
}

# This is supplied instead of the default because wxPython build.py doesn't grok the 'base-build' option.
# It instead uses {destdir}/${prefix} pair.

do_install() {
        cd ${S}
        install -d ${D}${PYTHON_SITEPACKAGES_DIR}

        STAGING_INCDIR=${STAGING_INCDIR} \
        STAGING_LIBDIR=${STAGING_LIBDIR} \
        PYTHONPATH=${D}${PYTHON_SITEPACKAGES_DIR} \
        ${STAGING_BINDIR_NATIVE}/${PYTHON_PN}-native/${PYTHON_PN} setup.py install --install-lib=${D}/${PYTHON_SITEPACKAGES_DIR} ${DISTUTILS_INSTALL_ARGS} ${CROSS_COMPILE_ARGS} || \
        bbfatal_log "'${PYTHON_PN} setup.py install ${DISTUTILS_INSTALL_ARGS} ${CROSS_COMPILE_ARGS}' execution failed."

        # support filenames with *spaces*
        find ${D} -name "*.py" -exec grep -q ${D} {} \; \
                               -exec sed -i -e s:${D}::g {} \;

        for i in ${D}${bindir}/* ${D}${sbindir}/*; do
            if [ -f "$i" ]; then
                sed -i -e s:${PYTHON}:${USRBINPATH}/env\ ${DISTUTILS_PYTHON}:g $i
                sed -i -e s:${STAGING_BINDIR_NATIVE}:${bindir}:g $i
            fi
        done

        rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/easy-install.pth

        #
        # FIXME: Bandaid against wrong datadir computation
        #
        if [ -e ${D}${datadir}/share ]; then
            mv -f ${D}${datadir}/share/* ${D}${datadir}/
            rmdir ${D}${datadir}/share
        fi
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

