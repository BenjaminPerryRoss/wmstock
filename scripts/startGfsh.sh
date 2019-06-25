echo Starting Geode Cluster

export PATH=$PATH:~/workspace/geode/geode-assembly/build/install/apache-geode/bin

echo $PATH

gfsh -e "run --file=scripts/startClusterGfsh.gfsh"

