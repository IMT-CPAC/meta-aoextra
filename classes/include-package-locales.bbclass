#
# Before image is created, examine the INCLUDE_PACKAGE_LOCALES variable and add all
# specified to the image.  Kinda like IMAGE_LINGUAS, but confined to packages.
#
do_include_package_locales() {

    for package in "${INCLUDE_PACKAGE_LOCALES}"; do
        bbwarn "Include locales for $package"
    done
}

IMAGE_PREPROCESS_COMMAND += "do_include_package_locales;"

