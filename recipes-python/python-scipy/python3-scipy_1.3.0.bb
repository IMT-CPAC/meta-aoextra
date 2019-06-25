inherit setuptools3 distutils3
require python-scipy.inc

RDEPENDS_${PN} += " \
	python3				\
	python3-numpy			\
"

DEPENDS += " \
	python3-numpy-native		\
	python3-distutils-extra-native	\
"
