#
# If first time build, preserve a copy of the image manifest in the user build directory.
# If a subsequent build, make copies of all .deb packages that are different from the
# original manifest and place them in the updated-debs directory in the build directory.
# Create a list 'uninstall' of files that are no longer in the package set.
#
# !!! CAN I RECAST THIS IN PYTHON SCRIPT ??? !!!
do_update_management() {
    rm -rf ./updated-debs
    if [ ! -e ./baseline-${IMAGE_BASENAME}-${MACHINE}.manifest ]; then
        # Copy original manifest
        cp ${IMAGE_MANIFEST} ./baseline-${IMAGE_BASENAME}-${MACHINE}.manifest
    else
        # Start with a fresh updated folder
        mkdir ./updated-debs

        # Tack a requried_release into the package area
        if [ "${UPDATE_ONLY_FROM_VERSION}" != "" ]; then
            echo "${UPDATE_ONLY_FROM_VERSION}" >> ./updated-debs/update-only-from-version
        fi

        # A BUG HERE> If a package is in not in the current manifest but in the baseline,
        # the file is INCLUDED rather than REMOVED.

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
                echo `basename ${pathname%.deb}` >> ./updated-debs/uninstall
            fi
        done

        # any package in FORCED_UPDATE is also moved if not already there
        for package in ${FORCED_UPDATE}; do
            # bbwarn "looking at '${package}'"
            tr ' ' '_' <./baseline-${IMAGE_BASENAME}-${MACHINE}.manifest | grep "^${package}_" | while read -r line; do
                # bbwarn "processing '${line}'"
                if [ -n "${line}" ]; then
                    bbwarn "forced update for ${line}"
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
                        bbwarn "${pathname} not found"
                    fi
                fi
            done
        done
    fi
}

IMAGE_POSTPROCESS_COMMAND += "do_update_management;"

