##RxJava使用注意事项
1. 保留一个Context的副本会因Observables导致内存泄露。如果Observable不能按时完成，可能会保留很多额外的内存。      
   解决方法： 采用CompositeSubscription，在根Activity/Fragment中添加模板代码，例如：

----------

	private CompositeSubscription mCompositeSubscription;
    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        return this.mCompositeSubscription;
    }


    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }
	@Override 
	protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }