#
# If first time build, preserve a copy of the image manifest in the user build directory.
# If a subsequent build, make copies of all .deb packages that are different from the
# original manifest and place them in the updated-debs directory in the build directory.
# 
do_update_management() {
    rm -rf ./updated-debs
    if [ ! -e ./baseline-${IMAGE_BASENAME}-${MACHINE}.manifest ]; then
        # Copy original manifest
        cp ${IMAGE_MANIFEST} ./baseline-${IMAGE_BASENAME}-${MACHINE}.manifest
    else
        # Start with a fresh updated folder
        mkdir ./updated-debs

        # Any deb not in baseline is added to updated-debs
        for line in `cat ${IMAGE_MANIFEST} ./baseline-${IMAGE_BASENAME}-${MACHINE}.manifest | sort | uniq -u | tr ' ' '_'`; do
           echo "line = ${line}"
           name="$(echo ${line} | cut -d'_' -f1)"
           section="$(echo ${line} | cut -d'_' -f2)"
           version="$(echo ${line} | cut -d'_' -f3 | sed -e 's/[0-9]*://')"
           if [ "${section}" = "all" ]; then
               arch="all"
           else
               arch="${DPKG_ARCH}"
           fi
           pathname="./tmp/deploy/deb/${section}/${name}_${version}_${arch}.deb"
           if [ -e ${pathname} ]; then
              cp ${pathname} ./updated-debs
           else
              echo ${pathname} >> ./updated-debs/removed-files
           fi
        done
    fi
}

IMAGE_POSTPROCESS_COMMAND += "do_update_management;"

