#
# If first time build, preserve a copy of the image manifest in the user build directory.
# If a subsequent build, make copies of all .deb packages that are different from the
# original manifest and place them in the updated-debs directory in the build directory.
# Create a list 'uninstall' of files that are no longer in the package set.
#
# !!! CAN I RECAST THIS IN PYTHON SCRIPT ??? !!!
do_update_management() {
    BASELINE_MANIFEST=./baseline-${IMAGE_BASENAME}-${MACHINE}.manifest

    rm -rf ./updated-debs-${IMAGE_BASENAME}
    if [ ! -e ${BASELINE_MANIFEST} ]; then
        # Copy original manifest
        cp ${IMAGE_MANIFEST} ${BASELINE_MANIFEST}
    else
        # Start with a fresh updated folder
        mkdir ./updated-debs-${IMAGE_BASENAME}

        # Tack a required_release into the package area
        if [ "${UPDATE_ONLY_FROM_VERSION}" != "" ]; then
            echo "${UPDATE_ONLY_FROM_VERSION}" >> ./updated-debs-${IMAGE_BASENAME}/update-only-from-version
        fi

        # bbwarn "OMIT_UPDATE contains ${OMIT_UPDATE}"

        # Use diff to extract the added and deleted lines in the baseline manifest
        # Make sorted copies of original and current manifest
        SORTED_BASELINE_MANIFEST=`mktemp`
        SORTED_IMAGE_MANIFEST=`mktemp`
        sort < ${BASELINE_MANIFEST} > ${SORTED_BASELINE_MANIFEST}
        sort < ${IMAGE_MANIFEST} > ${SORTED_IMAGE_MANIFEST}
 
        for line in `diff ${SORTED_BASELINE_MANIFEST} ${SORTED_IMAGE_MANIFEST} | tr ' ' '_'`; do
            type="$(echo ${line} | cut -d'_' -f1)"
            if [ "${type}" = "<" -o "${type}" = ">" ]; then
                name="$(echo ${line} | cut -d'_' -f2)"
                section="$(echo ${line} | cut -d'_' -f3)"
                version="$(echo ${line} | cut -d'_' -f4 | sed -e 's/[0-9]*://')"

                if [ "${section}" = "all" ]; then
                    arch="all"
                else
                    arch="${DPKG_ARCH}"
                fi
                pathname="./tmp/deploy/deb/${section}/${name}_${version}_${arch}.deb"

                # If name is in OMIT_UPDATE, don't add it do the package list
                # bbwarn "Checking for ${name} in ${OMIT_UPDATE}"
                omitted="n"
                for pattern in ${OMIT_UPDATE}; do
                    if echo ${name} | grep -w ${pattern}; then
                        # bbwarn "Omitting ${pathname}"
                        omitted="y"
                        break
                    fi
                done

                # If found in original and not in current, add to the uninstall list
                if [ "${type}" = "<" ]; then
                    # bbplain "Removed: `basename ${pathname}` from installed files"
                    # In origina; not in new manifest: add to uninstall list
                    echo `basename ${pathname%.deb}` >> ./updated-debs-${IMAGE_BASENAME}/uninstall

                elif [ "${omitted}" = "n" ]; then
                    # Was not in the original manifest and not specifically omitted, so add to install.
                    if [ -e ${pathname} ]; then
                        # bbplain "Added: `basename ${pathname}` from installed files"
                        cp ${pathname} ./updated-debs-${IMAGE_BASENAME}
                    fi
                # else
                    # bbplain "Omitted: `basename ${pathname}` from installed files"
                fi
            fi
        done

        # Remove sorted temp copies
        rm -f ${SORTED_BASELINE_MANIFEST} ${SORTED_IMAGE_MANIFEST}

        # any package in FORCE_UPDATE is also moved if not already there
        # bbwarn "FORCE_UPDATE contains ${FORCE_UPDATE}"
        for package in ${FORCE_UPDATE}; do
            # bbwarn "looking at '${package}'"
            tr ' ' '_' < ${IMAGE_MANIFEST} | grep "^${package}_" | while read -r line; do
                # bbwarn "processing '${line}'"
                if [ -n "${line}" ]; then
                    # bbplain "Forcing update for ${line}"
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
                        cp ${pathname} ./updated-debs-${IMAGE_BASENAME}/
                    else
                        bberror "FORCE_UPDATE: ${pathname} not found"
                    fi
                fi
            done
        done
    fi
}

IMAGE_POSTPROCESS_COMMAND += "do_update_management;"

