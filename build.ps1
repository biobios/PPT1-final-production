# Set the path to the Java compiler (javac) and the Java Archive Tool (jar)
$javaCompiler = "javac"
$jarTool = "jar"

# Set the source directory containing the Java files
$sourceDirectory = "src"
$sources = Get-ChildItem -Path $sourceDirectory -Filter "*.java" -Recurse

# Set the output directory for the compiled classes
$classDirectory = "classes"

# Manifest file
$manifestFile = "META-INF\MANIFEST.MF"

# Set the name of the JAR file
$jarFile = "SnakeGame.jar"

# Compile the Java files
& $javaCompiler -d $classDirectory -sourcepath $sourceDirectory $sources

# Create the JAR file
& $jarTool cfm $jarFile $manifestFile -C $classDirectory .