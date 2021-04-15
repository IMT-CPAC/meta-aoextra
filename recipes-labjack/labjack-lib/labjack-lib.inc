DESCRIPTION = "Installer for labjack shared libary"
SECTION = "libs"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LabJackM/LICENSE;md5=16841bcabc2cc9d3bf856d74b6562b68"

VERSION_MAJOR        = "${@d.getVar('PV', True).split('.')[0]}"
VERSION_MIDDLE       = "0${@d.getVar('PV', True).split('.')[1]}"
VERSION_MINOR        = "0${@d.getVar('PV', True).split('.')[2]}"
VERSION_MIDDLE_FIXED = "${@d.getVar('VERSION_MIDDLE', True)[-2:]}"
VERSION_MINOR_FIXED  = "${@d.getVar('VERSION_MINOR', True)[-2:]}"

VERSION = "${VERSION_MAJOR}.${VERSION_MIDDLE_FIXED}${VERSION_MINOR_FIXED}"

S = "${WORKDIR}/LabJackM-${VERSION}-Raspbian-Linux-armhf"

# Avoid the 'stripped' warning for the library
INSANE_SKIP_${PN} = "ldflags textrel dev-so"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

SRC_URI = "\
	http://labjack.com/sites/default/files/software/LabJackM-${VERSION}-Raspbian-Linux-armhf-release.tar.gz		\
"

DEPENDS = "\
	libusb1		\
	zlib		\
"

# Where the share stuff goes
SHARE="/usr/local/share"

do_unpack_append() {
    bb.build.exec_func('do_unpack_installer', d)
}

do_unpack_installer() {
    ${S}/LabJackM.run --noexec
}

do_install() {
    # Library
    install -d ${D}${libdir}
    install -m 0644 ${WORKDIR}/LabJackM/libLabJackM.so.${PV} ${D}${libdir}
    ln -s -r ${D}${libdir}/libLabJackM.so.${PV} ${D}${libdir}/libLabJackM.so

    # udev rules
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/LabJackM/*.rules ${D}${sysconfdir}/udev/rules.d

    # Configuration
    install -d ${D}/${SHARE}/LabJack/LJM
    install -m 0644 ${WORKDIR}/LabJackM/LabJack/LJM/*.json ${D}${SHARE}/LabJack/LJM/

    # Include for dev
    install -d ${D}${includedir}
    install -m 0644 ${WORKDIR}/LabJackM/LabJackM.h ${D}${includedir}
}

FILES_${PN} = "${libdir} ${sysconfdir} ${SHARE}"

FILES_${PN}-dev = "${includedir}"

