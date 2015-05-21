ViewScrollHelper
=====
Show/hide views (e.g. a toolbar) when the user scrolls up/down a RecyclerView/ObservableScrollView.

##Importing to your project
Add this dependency to your build.gradle file:
```java
dependencies {
    compile 'com.hrules:viewscrollhelper:1.1.0'
}
```
##Usage
```java
new ViewScrollHelper(recyclerView, toolbar);

new ViewScrollHelper(observableScrollView, toolbar);
```
More detailed usage example can be found in example application.

Developed by
-------
Héctor de Isidro - hrules6872 [![Twitter](http://img.shields.io/badge/contact-@h_rules-blue.svg?style=flat)](http://twitter.com/h_rules)

Based on [mzgreen's](https://github.com/mzgreen/HideOnScrollExample) work. Thank you!

License
-------
    Copyright 2015 Héctor de Isidro - hrules6872

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
