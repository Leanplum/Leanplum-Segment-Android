####################################################################
#
# Rules used to build and release the SDK.
#
####################################################################

version = $(shell cat sdk-version.txt)
gradleArgs = -PbintrayPackageVersion=$(version) -PartifactoryUser=$(artifactoryUser) -PartifactoryPassword=$(artifactoryPass) -PsonatypeOssrhUser=$(sonatypeUser) -PsonatypeOsshrPassword=$(sonatypePassword) -PsonatypeSigningKeyPassword=$(keyPass) -PsonatypeSigningKeyBase64=$(keyBase64)

verifyArguments:
	test $(artifactoryUser) || (echo "\nArgument missing: artifactoryUser\n" ; exit 1)
	test $(artifactoryPass) || (echo "\nArgument missing: artifactoryPass\n" ; exit 1)
	test $(sonatypeUser) || (echo "\nArgument missing: sonatypeUser\n" ; exit 1)
	test $(sonatypePassword) || (echo "\nArgument missing: sonatypePassword\n" ; exit 1)
	test $(keyPass) || (echo "\nArgument missing: keyPass\n" ; exit 1)
	test $(keyBase64) || (echo "\nArgument missing: keyBase64\n" ; exit 1)

tagCommit:
	git tag $(version)
	git push origin $(version)

deployPackage:
	./gradlew $(gradleArgs) assembleRelease generatePomFileForAarPublication artifactoryPublish publishAarPublicationToSonatypeRepository

deploy: verifyArguments tagCommit deployPackage
