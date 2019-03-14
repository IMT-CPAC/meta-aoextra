
# Postprocess the image and collect all debs in tmp/deploy/debs/*/*.deb.
# Two support directories exist (or are created) to facilitate generation of updates.
# If ${UPDATE_MANAGEMENT_CACHE} exists, debs that are different to those in this directory
# are copied to updated-debs.  If ${UPDATE_MANAGEMENT_CACHE}  does not exist, it is created
# and filled with a copy of the files in the deployed debs dir.
# 
do_update_management() {
    BUILDDIR=`pwd`
    echo BUILDDIR=${BUILDDIR}
    if [ ! -d ${BUILDDIR}/${UPDATE_MANAGEMENT_CACHE} ]; then
        # Create cache and link deployed debs to it.
        mkdir ${BUILDDIR}/${UPDATE_MANAGEMENT_CACHE}
        cd ${BUILDDIR}/tmp/deploy/deb
        for file in */*.deb; do
            mkdir -p ${BUILDDIR}/${UPDATE_MANAGEMENT_CACHE}/$file
            cp $file ${BUILDDIR}/${UPDATE_MANAGEMENT_CACHE}/$file
        done
        rm -rf ${BUILDDIR}/updated-debs
    else
        # Start with a fresh updated folder
        rm -rf ${BUILDDIR}/updated-debs
        mkdir ${BUILDDIR}/updated-debs

        # Any file that doesn't exist in the original is copied.
        cd ${BUILDDIR}/tmp/deploy/deb
        for file in */*.deb; do
           if [ ! -e ${BUILDDIR}/${UPDATE_MANAGEMENT_CACHE}/$file ]; then
               cp $file ${BUILDDIR}/updated-debs/`basename $file`
           fi
        done
    fi
    cd ${BUILDDIR}
}

IMAGE_POSTPROCESS_COMMAND = "do_update_management"

