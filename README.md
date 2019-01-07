# mediaplayer

Add it in your root build.gradle at the end of repositories:
Step 1. Add the JitPack repository to your build file

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
	
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.wwwVolley:medialib:1.0'
	}
	
	
Step 3. init player 

	mPlayerView.init()
                .setVideoPath(path)
                .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_HIGH)
                .enableDanmaku()
                .start();

