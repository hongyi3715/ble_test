### 当前的一些问题


## 1.viewModel的流程

####  基础内容
###### 1.ViewModel 的基本作用是什么？解决了什么问题？
viewModel的基本作用是用来临时保存展示的ui数据，解决了以往由于配置变更导致activity重建，需要对数据进行保存和恢复的繁琐问题


2.ViewModel 与 Activity/Fragment 的生命周期关系？
Activity 和 Fragment都各自实现了ViewModelStoreOwner接口，内部保存了viewModelStore这个hashmap，
hashmap保存了所有的viewModel,在Activity的onDestroy时会判断当前Activity是否为正常关闭，如果是正常关闭
则同时清理viewModel

3.为什么推荐使用 ViewModel + LiveData 的组合？


4.ViewModel 如何在配置变更（如屏幕旋转）时保持数据？
通过在Activity配置变更时，onRetainNonConfigurationInstance方法，将viewModelStore保存到NonConfigurationInstance中，
通过getViewModelStore进行恢复


