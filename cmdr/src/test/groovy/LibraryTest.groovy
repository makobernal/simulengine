/*
 * This Spock specification was auto generated by running the Gradle 'init' task
 * by 'jbe35' at '12/5/16 11:46 AM' with Gradle 3.2.1
 *
 * @author jbe35, @date 12/5/16 11:46 AM
 */

import spock.lang.Specification

class LibraryTest extends Specification{
    def "someLibraryMethod returns true"() {
        setup:
        Library lib = new Library()
        when:
        def result = lib.someLibraryMethod()
        then:
        result == true
    }
}
