#
# Goes through selected vars and process wildcards to fill out the vars with explicit values
#
do_process_package_wildcards() {

    for var in ${PROCESS_PACKAGE_WILDCARDS}; do
        bbwarn "Process package wildcards in ${var}"
        bbwarn "Looking in ${WORKDIR}/packages-split"
        for package in `ls -d ${WORKDIR}/packages-split/*`; do
            bbwarn "Package: `basename ${package}`"
        done
    done
}

PACKAGE_PREPROCESS_FUNCS += "do_process_package_wildcards"

