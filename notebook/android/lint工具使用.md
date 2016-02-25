####[lint使用](http://developer.android.com/tools/debugging/improving-w-lint.html)
	android {
	    lintOptions {
	       // set to true to turn off analysis progress reporting by lint
	       quiet true
	       // if true, stop the gradle build if errors are found
	       abortOnError false
	       // if true, only report errors
	       ignoreWarnings true
	       }
	       ...
	    }

	To manually run inspections in Android Studio, from the application or right-click menu,
	choose Analyze > Inspect Code. The Specify Inspections Scope dialog appears so you can
	specify the desired inspection scope and profile.

	lint project_name
	lint help
