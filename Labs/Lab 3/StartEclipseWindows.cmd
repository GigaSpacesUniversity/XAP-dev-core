@Echo off
@Call ..\..\..\SetEnv.cmd
SET BASE=%~dp0

type .metadata\.plugins\org.eclipse.core.runtime\.settings\org.eclipse.jdt.core.prefs | ..\..\..\software\utils\grep -v GIGASPACES_HOME > prefs
echo org.eclipse.jdt.core.classpathVariable.GIGASPACES_HOME=%BASE%../../../gigaspaces-xap | ..\..\..\software\utils\sed s/\\/\//g >> prefs
type .metadata\.plugins\org.eclipse.core.runtime\.settings\org.eclipse.jdt.core.prefs | ..\..\..\software\utils\grep -v XAP_TRAINING_HOME_LIB >> prefs
echo org.eclipse.jdt.core.classpathVariable.XAP_TRAINING_HOME_LIB=%BASE%../../../labs/lib | ..\..\..\software\utils\sed s/\\/\//g >> prefs

move prefs .metadata\.plugins\org.eclipse.core.runtime\.settings\org.eclipse.jdt.core.prefs


START ..\..\..\Eclipse\Windows\eclipse\eclipse.exe -data "%CD%"
        