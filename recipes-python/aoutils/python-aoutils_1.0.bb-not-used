#
# Recipe for visioncoach server.
#
inherit distutils setuptools

SRCREV = "${AUTOREV}"

OLD_PV := "${PV}"
PV = "${OLD_PV}+git${SRCPV}"

require python-aoutils.inc

DEPENDS += " \
	python-distutils-extra-native 	\
	python-dbus			\
	python-pygobject 		\
"

RDEPENDS_${PN} += " \
	python				\
	python-dbus			\
	python-threading		\
	python-pygobject 		\
"

