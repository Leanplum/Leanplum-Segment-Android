####################################################################
#
# Rules used to build and release the SDK.
#
####################################################################

version = $(shell cat sdk-version.txt)
gradleArgs = -PbintrayPackageVersion=$(version) -PbintrayUser=$(user) -PbintrayApiKey=$(key)

verifyArguments:
	test $(user) || (echo "\nArgument missing: user=YOUR-BINTRAY-USERNAME\n" ; exit 1)
	test $(key) || (echo "\nArgument missing: key=YOUR-BINTRAY-API-KEY\n" ; exit 1)

tagCommit:
	git tag $(version)
	git push origin $(version)

bintrayDeploy:
	./gradlew $(gradleArgs) assembleRelease generatePomFileForAarPublication bintrayUpload

deploy: verifyArguments tagCommit bintrayDeploy
