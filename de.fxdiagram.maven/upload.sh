#!/bin/sh
pushd .
cd bintray
read -s -p "Key: " key
echo
rm -f output.log
for file in `find * -type f -print`; do
	echo $file
	if [ $file == 'p2/updates/content.jar' ] || [ $file == 'p2/updates/artifacts.jar' ] ; then
		curl -# -T $file -ujankoehnlein:$key https://api.bintray.com/content/jankoehnlein/generic/$file > output.log
	else
		curl -# -T $file -ujankoehnlein:$key https://api.bintray.com/content/jankoehnlein/generic/FXDiagram/0.1/$file > output.log
	fi
	cat output.log
	echo
	rm -f output.log
done
popd
