
1. Think twice before adding any third party library, it’s a really serious commitment;

2. If the user can’t see it, [don’t draw it!](http://riggaroo.co.za/optimizing-layouts-in-android-reducing-overdraw/),过度重绘问题;

3. Don’t use a database unless you really need to;

4. Hitting the 65k method count mark is gonna happen fast, I mean really fast! And [multidexing can save you](https://medium.com/@rotxed/dex-skys-the-limit-no-65k-methods-is-28e6cb40cf71#.qkt9ao3ki);

5. RxJava is the best alternative to AsyncTasks and so much more;

6. Retrofit is the best networking library there is;

7. Shorten your code with [Retrolambda](https://medium.com/android-news/retrolambda-on-android-191cc8151f85#.s7bntwcrs);

8. [Combine RxJava with Retrofit and Retrolambda for maximum awesomeness!](https://medium.com/swlh/party-tricks-with-rxjava-rxandroid-retrolambda-1b06ed7cd29c);

9. I use EventBus and it’s great, but I don’t use it too much because the codebase would get really messy;

9. [Package by Feature, not layers](https://medium.com/the-engineering-team/package-by-features-not-layers-2d076df1964d#.2ufakamtb),功能划分包;

10. Move everything off the application thread;

12. [lint your views to help you optimize the layouts and layout hierarchies so you can identify  redundant views that could perhaps be removed](http://developer.android.com/tools/debugging/improving-w-lint.html);

13. If you’re using gradle, speed it up anyway you [can](https://medium.com/the-engineering-team/speeding-up-gradle-builds-619c442113cb#.ttdek81ke);

14. Do profile reports of your builds to see what is taking the build time;

15. [Use a well known architecture](http://fernandocejas.com/2015/07/18/architecting-android-the-evolution/);

16. [Testing takes time but it’s faster and more robust than coding without tests once you’ve got the hang of it;](http://stackoverflow.com/questions/67299/is-unit-testing-worth-the-effort/67500#67500)

17. [Use dependency injection to make your app more modular and therefore easier to test;](http://fernandocejas.com/2015/04/11/tasting-dagger-2-on-android/)

18. [Listening to fragmented podcast will be great for you](http://fragmentedpodcast.com/);

19. [Never use your personal email for your android market publisher account;](https://www.reddit.com/r/Android/comments/2hywu9/google_play_only_one_strike_is_needed_to_ruin_you/)

20. [Always use appropriate input types](http://developer.android.com/training/keyboard-input/style.html);

21. Use analytics to find usage patterns and isolate bugs;

22. Stay on top of new [libraries](http://android-arsenal.com/) (use [dryrun](https://github.com/cesarferreira/dryrun) to test them out faster);

23. Your services should do what they need to do and die as quickly as possible;

24. Use the [Account Manager](http://developer.android.com/reference/android/accounts/AccountManager.html) to suggest login usernames and email addresses;

25. Use CI (Continuous Integration) to build and distribute your beta and production .apk’s;

26. Don’t run your own CI server, maintaining the server is time consuming because of disk 

27. space/security issues/updating the server to protect from SSL attacks, etc. Use circleci, travis or shippable, they’re cheap and it’s one less thing to worry about;

27. [Automate your deployments to the playstore;](https://github.com/Triple-T/gradle-play-publisher)

28. If a library is massive and you are only using a small subset of its functions you should find an alternative smaller option ([rely on proguard for instance](http://developer.android.com/tools/help/proguard.html));

29. Don’t use more modules than you actually need. If that modules are not constantly modified, it’s important to have into consideration that the time needed to compile them from scratch (CI builds are a good example), or even to check if the previous individual module build is up-to-date, can be up to almost 4x greater than to simply load that dependency as a binary .jar/.aar.

30. [Start thinking about ditching PNGs for SVGs;](http://developer.android.com/tools/help/vector-asset-studio.html)

31. Make library abstraction classes, it’ll be way easier to switch to a new library if you only need to switch in one place (e.g. AppLogger.d(“message”) can contain Log.d(TAG, message) and later realise that Timber.d(message) is a better option);

32. Monitor connectivity and type of connection (more data updates while on wifi?);

33. Monitor power source and battery (more data updates while charging? Suspend updates when battery is low?);

34. A user interface is like a joke. If you have to explain it, it’s not that good;

35. Tests are great for performance: Write slow (but correct) implementation then verify optimizations don’t break anything with tests.