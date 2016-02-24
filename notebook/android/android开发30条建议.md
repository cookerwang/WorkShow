
1. Think twice before adding any third party library, it’s a really serious commitment;
2. If the user can’t see it, [don’t draw it!](http://riggaroo.co.za/optimizing-layouts-in-android-reducing-overdraw/),过度重绘问题;
3. Don’t use a database unless you really need to;
4. Hitting the 65k method count mark is gonna happen fast, I mean really fast! And multidexing can save you;
5. RxJava is the best alternative to AsyncTasks and so much more;
6. Retrofit is the best networking library there is;
7. Shorten your code with Retrolambda;
8. Combine RxJava with Retrofit and Retrolambda for maximum awesomeness!;
9. I use EventBus and it’s great, but I don’t use it too much because the codebase would get really messy;
9. [Package by Feature, not layers](https://medium.com/the-engineering-team/package-by-features-not-layers-2d076df1964d#.2ufakamtb),功能划分包;
10. Move everything off the application thread;
12. lint your views to help you optimize the layouts and layout hierarchies so you can identify  redundant views that could perhaps be removed;
13. If you’re using gradle, speed it up anyway you can;
14. Do profile reports of your builds to see what is taking the build time;
15. Use a well known architecture;
16. Testing takes time but it’s faster and more robust than coding without tests once you’ve got the hang of it;
17. Use dependency injection to make your app more modular and therefore easier to test;
18. Listening to fragmented podcast will be great for you;
19. Never use your personal email for your android market publisher account;
20. Always use appropriate input types;
21. Use analytics to find usage patterns and isolate bugs;
22. Stay on top of new libraries (use dryrun to test them out faster);
23. Your services should do what they need to do and die as quickly as possible;
24. Use the Account Manager to suggest login usernames and email addresses;
25. Use CI (Continuous Integration) to build and distribute your beta and production .apk’s;
26. Don’t run your own CI server, maintaining the server is time consuming because of disk space/security issues/updating the server to protect from SSL attacks, etc. Use circleci, travis or shippable, they’re cheap and it’s one less thing to worry about;
27. Automate your deployments to the playstore;
28. If a library is massive and you are only using a small subset of its functions you should find an alternative smaller option (rely on proguard for instance);
29. Don’t use more modules than you actually need. If that modules are not constantly modified, it’s important to have into consideration that the time needed to compile them from scratch (CI builds are a good example), or even to check if the previous individual module build is up-to-date, can be up to almost 4x greater than to simply load that dependency as a binary .jar/.aar.
30. Start thinking about ditching PNGs for SVGs;
31. Make library abstraction classes, it’ll be way easier to switch to a new library if you only need to switch in one place (e.g. AppLogger.d(“message”) can contain Log.d(TAG, message) and later realise that Timber.d(message) is a better option);
32. Monitor connectivity and type of connection (more data updates while on wifi?);
33. Monitor power source and battery (more data updates while charging? Suspend updates when battery is low?);
34. A user interface is like a joke. If you have to explain it, it’s not that good;
35. Tests are great for performance: Write slow (but correct) implementation then verify optimizations don’t break anything with tests.