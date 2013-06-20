#!/bin/sh
. ../../../SetEnvMac.sh
BASE=`dirname $0`
BASE=`pwd $BASE`

# Filter out the GIGASPACES_HOME from the prefs and calculate the location
cat .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.jdt.core.prefs | grep -v GIGASPACES_HOME > prefs
echo org.eclipse.jdt.core.classpathVariable.GIGASPACES_HOME=$BASE/../../../gigaspaces-xap >> prefs
cat .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.jdt.core.prefs | grep -v XAP_TRAINING_HOME_LIB >> prefs
echo org.eclipse.jdt.core.classpathVariable.XAP_TRAINING_HOME_LIB=$BASE/../../../labs/lib >> prefs

mv prefs .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.jdt.core.prefs

../../../eclipse/mac/eclipse/eclipse -data "$BASE"
        
