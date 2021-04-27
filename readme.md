
# Android Architecture Components Tutorial

Android architecture components are a collection of libraries that help you design robust, testable, and maintainable apps. Start with classes for managing your UI component lifecycle and handling data persistence. (https://developer.android.com/topic/libraries/architecture)

This tutorial will cover LiveData, ViewModel, Room, DataBinding and some other features in Android Architecture Component, and build a photo browser app according to the best practice from the [Guide to app architecture](https://developer.android.com/jetpack/guide).

## Prerequisite

This app will fetch photos from [Pexels](https://www.pexels.com/), so the before starting build the app, we need to create a Pexels account if you don’t have one. We won’t focus on how to create the Pexels account, because it is out of scope of this tutorial. But It is pretty easy to create new account based on the instruction on Pexels.

We need to save the Authorization Token in safe place, which is the key to access Pexels API and will be used in the app.

Then we can do some testing to the Pexels API with Postman to make sure our Authorization Token is correct and we are able to fetch photos for Pexels.

![](https://cdn-images-1.medium.com/max/7616/1*2Ar3u5AqO3kfPZ2JZCN8nQ.png)

Pay attention you need to replace the Authorization Token in the Headers with the real value you got when you created the Pexels account.

And then if we are lucky, we will get a response in JSON format like this:

    {

    "page": 1,

    "per_page": 2,

    "photos": [

    {

    "id": 6546164,

    "width": 3265,

    "height": 5000,

    "url": "https://www.pexels.com/photo/person-taking-bowl-with-hawaiian-dish-6546164/",

    "photographer": "Larissa Deruzzi",

    "photographer_url": "https://www.pexels.com/@deruzzi",

    "photographer_id": 20198481,

    "avg_color": "#BE695C",

    "src": {

    "original": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg",

    "large2x": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",

    "large": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&h=650&w=940",

    "medium": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&h=350",

    "small": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&h=130",

    "portrait": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=1200&w=800",

    "landscape": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=627&w=1200",

    "tiny": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"

    },

    "liked": **false**

    },

    {

    "id": 6805855,

    "width": 5126,

    "height": 4101,

    "url": "https://www.pexels.com/photo/group-of-tourists-walking-on-snowy-hilly-terrain-6805855/",

    "photographer": "Harry Cooke",

    "photographer_url": "https://www.pexels.com/@harry-cooke",

    "photographer_id": 3933683,

    "avg_color": "#738080",

    "src": {

    "original": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg",

    "large2x": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",

    "large": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&h=650&w=940",

    "medium": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&h=350",

    "small": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&h=130",

    "portrait": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=1200&w=800",

    "landscape": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=627&w=1200",

    "tiny": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"

    },

    "liked": **false**

    }

    ],

    "total_results": 8000,

    "next_page": "https://api.pexels.com/v1/search/?page=2&per_page=2&query=people"

    }

If we analyse the JSON object, we would get the schema is

    {
    page: Int,
    per_page: Int,
    photos: List<Photo>,
    total_result: int,
    next_page: String
    }

and the schema of Photo is

    {
    id: Int,
    width: Int,
    height: Int,
    url: String,
    photographer: String,
    photographer_url: String,
    photographer_id: Int,
    avg_color: String,
    src: Src,
    liked: Boolean,
    }

and the schema of Src is

    {
    original: String,
    large2x: String,
    large: String,
    medium: String,
    small: String,
    portrait: String,
    landscape: String,
    tiny: String
    }

We need these schemas when we create data model

And then we can start to build the app once we verified the Pexels API is accessible.

## Create new project

Let us open Android Studio and select Create a new project from the menu or launch wizard and choose Bottom Navigation Activity template

![](https://cdn-images-1.medium.com/max/4048/1*4D-BTtUYq7MwL3ur4WjfJQ.png)

and the click next and complete the configuration page like this:

![](https://cdn-images-1.medium.com/max/4048/1*mZzCKZjigs1nnwK2Jqdg8Q.png)

and then click finish button.

Then the Android Studio would start build this new project and download some dependencies. It might take several minutes.

### Run the empty app

We need to create a new emulator if we don’t have any, and launch the emulator, and then run the app. It would show a UI like this:

![](https://cdn-images-1.medium.com/max/2000/1*cjWYEkV3_XEvY_tNUKghPw.png)

It is an empty app from the Bottom Navigation Activity template, which contains 3 pages, Home, Dashboard and Notification and the Home page is displaying when the app launches.

And then we will implement the app step by step in upcoming chapters

## Create Data Model

We would build this app on MVVM architecture, so let’s implement the Model first.

Firstly, we need to add following dependencies to Build.gradle(Module: Pexels.app)

    *// Moshi
    *implementation 'com.squareup.moshi:moshi-kotlin:1.9.3'

Sync Now as Android Studio instructed

Secondly, let’s create a new package, data for sake of tidiness of the whole project.

And then we need to create several data classes, Data, Data.Photo and Data.Photo.Src according the schemas of the JSON object we got in Prerequisite step.

So let’s create a new file, Data.kt in the data package, and add data class as followed:

    package com.ovlesser.pexels.data
    
    data class Data(
        val page: Int,
        @Json(name = "per_page") val perPage: Int,
        val photos: List<Photo>,
        @Json(name = "total_results") val totalResults: Int,
        @Json(name = "next_page") val nextPage: String
    ) {
        data class Photo(
            val id: Int,
            val width: Int,
            val height: Int,
            val url: String,
            val photographer: String,
            @Json(name = "photographer_url") val photographerUrl: String,
            @Json(name = "photographer_id") val photographerId: Int,
            @Json(name = "avg_color") val avgColor: String,
            val src: Src,
            val liked: Boolean,
        ) {
            data class Src(
                val original: String,
                val large2x: String,
                val large: String,
                val medium: String,
                val small: String,
                val portrait: String,
                val landscape: String,
                val tiny: String
            )
        }
    }

Be careful the annotations like @Json(name = "photographer_url"), which maps the name in the JSON object to the name in the data class

Then we have created the data models, and completed the Model of the MVVM. We will implement the ViewModel in next chapter

## Create ViewModel for home page

You might notice there has been a file named HomeViewModel.kt in the ./ui/home package, which is de default ViewModel generated along with the template. We need to replace the code of HomeViewModel class with

    *// The internal MutableLiveData Data that stores the most recent data
    *private val _data = MutableLiveData<Data>()
    
    *// The external immutable LiveData for the response Data
    *val data: LiveData<Data>
        get() = _data
    
    init {
        getDataFromSample()
    }
    
    private fun getDataFromSample() {
        _data.value = Data(0, 0, *emptyList*(), 0, "")
    }

And the Android Studio might ask you to solve Data by importing com.ovlesser.pexels.data.Data, just do it.

Next step, we would modify the getDataFromSample method with more meaningful data. Do you still remember the response when we test the Pexels API with Postman? We can use that response.

Let’s create a new file, SampleData.kt in the datapackage, and define a variable sampleData in it and copy and paste the response as the value of this string as followed:

    package com.ovlesser.pexels

    val *sampleData *=
        """
    {
        "page": 1,
        "per_page": 2,
        "photos": [
            {
                "id": 6546164,
                "width": 3265,
                "height": 5000,
                "url": "https://www.pexels.com/photo/person-taking-bowl-with-hawaiian-dish-6546164/",
                "photographer": "Larissa Deruzzi",
                "photographer_url": "https://www.pexels.com/@deruzzi",
                "photographer_id": 20198481,
                "avg_color": "#BE695C",
                "src": {
                    "original": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg",
                    "large2x": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    "large": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&h=650&w=940",
                    "medium": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&h=350",
                    "small": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&h=130",
                    "portrait": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=1200&w=800",
                    "landscape": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=627&w=1200",
                    "tiny": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
                },
                "liked": false
            },
            {
                "id": 6805855,
                "width": 5126,
                "height": 4101,
                "url": "https://www.pexels.com/photo/group-of-tourists-walking-on-snowy-hilly-terrain-6805855/",
                "photographer": "Harry Cooke",
                "photographer_url": "https://www.pexels.com/@harry-cooke",
                "photographer_id": 3933683,
                "avg_color": "#738080",
                "src": {
                    "original": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg",
                    "large2x": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                    "large": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&h=650&w=940",
                    "medium": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&h=350",
                    "small": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&h=130",
                    "portrait": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=1200&w=800",
                    "landscape": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=627&w=1200",
                    "tiny": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
                },
                "liked": false
            }
        ],
        "total_results": 8000,
        "next_page": "https://api.pexels.com/v1/search/?page=2&per_page=2&query=people"
    }
        """.*trimIndent*()

We would test out app with this sample data without implementing the network module.

So let’s modify the getDataFromSample function as shown below:

    private fun getDataFromSample() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter = moshi.adapter(Data::class.*java*)
        val sampleData = jsonAdapter.fromJson(*sampleData*)
        _data.*value *= sampleData ?: Data(0, 0, *emptyList*(), 0, "")
    }

We are converting the sample data from a JSON string to a Data object with Moshi. We will use same way to convert the data from Pexels API to Data object later.

Now, we have implemented the skeleton of the ViewModel of Home page. We would implement the View in next chapter

## Build the View of the Home page

We will use the Home fragment as out main page, in which we will display the photos with the grid layout.

Firstly, we need to add some UI related dependencies. Let’s open the Build.gradle(Module: Pexels.app) file and add several extra lines into the plugins section as followed:

plugins **{
 **id 'com.android.application'
 id 'kotlin-android'
** id 'kotlin-android-extensions'
 id 'kotlin-kapt'
}**

The **bold** lines are newly added.

And we also need to enable the DataBinding by add following lines under the buildTypes section,

    buildTypes **{
        **release **{
            **minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        **}
    }
    buildFeatures {
        dataBinding true
    }
    **compileOptions **{
        **sourceCompatibility JavaVersion.*VERSION_1_8
        *targetCompatibility JavaVersion.*VERSION_1_8
    ***}
    **kotlinOptions **{
        **jvmTarget = '1.8'
    **}**

and add following dependencies into dependencies section.

    *//RecyclerView
    *implementation "androidx.recyclerview:recyclerview:1.1.0"
    *// For control over item selection of both touch and mouse driven selection
    *implementation "androidx.recyclerview:recyclerview-selection:1.1.0"

And we need to click Sync now.

Secondly, we need to define the layout of the list item in the photo grid.

Let’s create a new layout file, named grid_view_item.xml in the layout folder and replace the content with following code

    *<?*xml version="1.0" encoding="utf-8"*?>
    
    *<layout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools">
    
        <data>
            <variable
                name="photo"
                type="com.ovlesser.pexels.data.Data.Photo" />
        </data>
    
        <FrameLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="4dp"
            android:minHeight="100dp">
    
            <ImageView
                android:id="@+id/pexel_image"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:minHeight="100dp"
                android:minWidth="100dp"
                android:scaleType="fitXY"
                android:adjustViewBounds="true"
                app:imageUrl="@{photo.src.medium}"
                android:background="@color/teal_700"
                tools:src="@tools:sample/backgrounds/scenic"/>
    
        </FrameLayout>
    </layout>

You might noticed we declared a variable, photo with com.ovlesser.pexels.data.Data.Photo type, which is the would pass value of photo to this layout file.

It would fail if you build the app because we haven’t implement the data binding of imageUrl. We will fix it later.

And then we need to modify the layout of the Home page itself. Let’s open fragment_home.xml and replace the whole TextView section with

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/photos_grid"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:padding="6dp"
        android:clipToPadding="false"
       app:layoutManager="androidx.recyclerview.widget.StaggeredGridLayoutManager"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:spanCount="2"
        tools:itemCount="16"
        tools:listitem="@layout/grid_view_item" />

This is a RecyclerView with StaggeredGridLayoutManager in which the photos would be displayed.

We also need to wrap the whole ConstraintLayout section inside a layout tag and move the properties of ConstraintLayout to layout tag as followed in order to use DataBinding

    *<?*xml version="1.0" encoding="utf-8"*?>
    ***<layout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools">
    **
        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            tools:context=".ui.home.HomeFragment">

            ......

       </androidx.constraintlayout.widget.ConstraintLayout>
    **</layout>**

Next step is to modify the HomeFragment.kt, which is the UI part of the Home fragment.

We need to change the definition of property homeViewModel from

    *private lateinit var homeViewModel: HomeViewModel*

to

    private val homeViewModel: HomeViewModel by *lazy ***{
        **ViewModelProvider(this).get(HomeViewModel::class.*java*)
    **}**

And we also need to replace the contents in the onCreateView method with

    val binding = FragmentHomeBinding.inflate(inflater)
    
    *// Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
    *binding.*lifecycleOwner *= this
    
    return binding.*root*

This piece of code means we would use DataBinding to pass data between source code and the layout.

We will implement an adapter for the RecyclerView in next step, which maps the list of photos into the list items.

Let’s create a new file, PhotoGridAdapter and implement it as followed:

    class PhotoGridAdapter: ListAdapter<Data.Photo, PhotoGridAdapter.PhotoGridViewHolder>(DiffCallback) {
    
        class PhotoGridViewHolder(private var binding: GridViewItemBinding): RecyclerView.ViewHolder(binding.*root*) {
            fun bind(photo: Data.Photo) {
                binding.photo = photo
                binding.executePendingBindings()
            }
        }
    
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoGridViewHolder {
            return PhotoGridViewHolder( GridViewItemBinding.inflate( LayoutInflater.from( parent.*context*)))
        }
    
        override fun onBindViewHolder(holder: PhotoGridViewHolder, position: Int) {
            val photo = getItem(position)
            holder.bind(photo)
        }
    
        companion object DiffCallback: DiffUtil.ItemCallback<Data.Photo>() {
            override fun areItemsTheSame(oldItem: Data.Photo, newItem: Data.Photo): Boolean {
                return oldItem.id === newItem.id
            }
    
            override fun areContentsTheSame(oldItem: Data.Photo, newItem: Data.Photo): Boolean {
                return oldItem == newItem
            }
    
        }
    }

And then we need to create another file, BindingAdapters.kt, and implement a function with annotation BindingAdapter("liveData"), which would be binding the adapter to the RecyclerView.

    @BindingAdapter("listData")
    fun bindRecyclerView(recyclerView: RecyclerView,
                         photos: List<Data.Photo>?) {
        val adapter = recyclerView.*adapter *as PhotoGridAdapter
        adapter.submitList(photos)
    }

And then let’s open fragment_home.xml and add data section above all components and add app:listData="@{viewModel.data.photos}" inside the RecyclerView to bind the ViewModel to the View as followed:

    *<?*xml version="1.0" encoding="utf-8"*?>
    *<layout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools">
    
    **    <data>
            <variable
                name="viewModel"
                type="com.ovlesser.pexels.ui.home.HomeViewModel" />
        </data>
    **
        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            tools:context=".ui.home.HomeFragment">
    
            <androidx.recyclerview.widget.RecyclerView
                ......
                app:spanCount="2"
                tools:itemCount="16"
    **            app:listData="@{viewModel.data.photos}"
    **            tools:listitem="@layout/grid_view_item" />
        </androidx.constraintlayout.widget.ConstraintLayout>
    </layout>

The name in the RecyclerView and the name of the BindingAdapter must be same, in this case, it is listData

And then, we need to open HomeFragment.kt and add following lines into onCreateView method as shown below.

    val binding = FragmentHomeBinding.inflate(inflater)
    
    *// Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
    *binding.*lifecycleOwner *= this
    
    ***// Giving the binding access to the OverviewViewModel
    *binding.viewModel = homeViewModel
    
    binding.photosGrid.*adapter *= PhotoGridAdapter()
    **
    return binding.*root*

Now the ViewModel is binding to the View of Home page, and an adapter is binging to the RecyclerView.

Next step, we will display the photo in the RecyclerView by binding imageUrl in the grid_view_item layout with another BindingAdapter.

Firstly, we need to add following dependency to build.gradle(Module:Pexels.app)

    *// Glide
    *implementation 'com.github.bumptech.glide:glide:4.11.0'

Secondly, we need to add following BindingAdapter to BindingAdapters.kt

    @BindingAdapter("imageUrl")
    fun bindImageView(imageView: ImageView, imageUrl: String?) {
        imageUrl?.*let ***{
            **val imageUri = imageUrl.*toUri*().buildUpon().scheme("https").build()
            Glide.with(imageView.*context*)
                .load(imageUri)
                .apply(
                    RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
                .into(imageView)
        **}
    **}

You might notice the image loading_animation and ic_broken_image are missing, so let’s create them or you may download it somewhere.

Let’s create a new drawable, loading_animation and replace its content with

    *<?*xml version="1.0" encoding="utf-8"*?>
    
    *<animated-rotate xmlns:android="http://schemas.android.com/apk/res/android"
        android:drawable="@drawable/loading_img"
        android:pivotX="50%"
        android:pivotY="50%" />

and create another drawable, loading_img and replace its content with

    <vector android:height="24dp" android:viewportHeight="72"
        android:viewportWidth="72" android:width="24dp" xmlns:android="http://schemas.android.com/apk/res/android">
        <path android:fillColor="#cccccf"
            android:pathData="M36.06,28.92L36.06,32.18"
            android:strokeColor="#e7e7e7" android:strokeLineCap="round" android:strokeWidth="1"/>
        <path android:fillColor="#c8c8cc"
            android:pathData="M39.45,29.88L37.82,32.71"
            android:strokeColor="#cacaca" android:strokeLineCap="round" android:strokeWidth="1"/>
        <path android:fillColor="#bbbbbe"
            android:pathData="M42.12,32.32L39.3,33.95"
            android:strokeColor="#cdcdcd" android:strokeLineCap="round" android:strokeWidth="1"/>
        <path android:fillColor="#b2b2b7"
            android:pathData="M39.8,35.98L43.06,35.98"
            android:strokeColor="#cbcbcb" android:strokeLineCap="round" android:strokeWidth="1"/>
        <path android:fillColor="#d0d0d4"
            android:pathData="M32.77,29.99L34.4,32.81"
            android:strokeColor="#ededed" android:strokeLineCap="round" android:strokeWidth="1"/>
        <path android:fillColor="#949497"
            android:pathData="M30.1,32.42L32.92,34.05"
            android:strokeColor="#525252" android:strokeLineCap="round" android:strokeWidth="1"/>
        <path android:fillColor="#97979b"
            android:pathData="M32.42,35.98L29.16,35.98"
            android:strokeColor="#6e6e6e" android:strokeLineCap="round" android:strokeWidth="1"/>
        <path android:fillColor="#a8a8ac"
            android:pathData="M36.06,43.08L36.06,39.82"
            android:strokeColor="#a0a0a0" android:strokeLineCap="round" android:strokeWidth="1"/>
        <path android:fillColor="#cacaca"
            android:pathData="M39.7,41.99L38.07,39.16"
            android:strokeColor="#cacaca" android:strokeLineCap="round" android:strokeWidth="1"/>
        <path android:fillColor="#b6b6ba"
            android:pathData="M42.19,39.4L39.37,37.77"
            android:strokeColor="#ccc" android:strokeLineCap="round" android:strokeWidth="1"/>
        <path android:fillColor="#a1a1a5"
            android:pathData="M32.46,41.98L34.09,39.16"
            android:strokeColor="#909090" android:strokeLineCap="round" android:strokeWidth="1"/>
        <path android:fillColor="#9d9da0"
            android:pathData="M29.85,39.4L32.67,37.77"
            android:strokeColor="#7a7a7a" android:strokeLineCap="round" android:strokeWidth="1"/>
    </vector>

and the third drawable, ic_broken_image, replace its content with

    <vector xmlns:android="http://schemas.android.com/apk/res/android"
        android:width="24dp"
        android:height="24dp"
        android:tint="#A9A9AC"
        android:viewportWidth="24.0"
        android:viewportHeight="24.0">
        <path
            android:fillColor="#FF000000"
            android:pathData="M21,5v6.59l-3,-3.01 -4,4.01 -4,-4 -4,4 -3,-3.01L3,5c0,-1.1 0.9,-2 2,-2h14c1.1,0 2,0.9 2,2zM18,11.42l3,3.01L21,19c0,1.1 -0.9,2 -2,2L5,21c-1.1,0 -2,-0.9 -2,-2v-6.58l3,2.99 4,-4 4,4 4,-3.99z" />
    </vector>

Ok. The error of missing symbol in the bindImageView function is fixed.

And it should be able to build after we fixed the the missing binding of imageUrl in the grid_view_item.xml.

Now we have bind all HomeViewModel and Data and HomeFragment with its adapter together. So you might want to run it. But it would fail with an error Permission denied (missing INTERNET permission?). Let’s fix it in next chapter.

## Update the User Permission

Let’s open AndroidManifest.xml and add permission above the application section as followed:

    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
        ......
    
        **<uses-permission android:name="android.permission.INTERNET" />**
        <application
            ......

Run it again and it would appear like this:

![](https://cdn-images-1.medium.com/max/2000/1*zrl3HryH9SiqnEssCipVCg.png)

But be careful, the photos are from our sampleData. We will add the real network operation in next chapter.

## Add Network Operation

We have displayed the photos in a grid with DataBinding on MVVM architecture in previous chapter, although the data are fake. So let’s fetch the real photos from Pexels API on the runtime. To implement it, we need to integrate Retrofit.

Firstly, we need to add following dependencies into build.gradle(Modules:Pexels.app)

    *// Retrofit
    *implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-scalars:2.9.0'
    implementation 'com.squareup.retrofit2:converter-moshi:2.9.0'

Sync Now

Secondly, we need to implement a network service. Let’s create a new package, network, to keep the code clean and tidy, then create a new interface, PexelsApiService, in this package, and replace it with following code

    private val *BASE_URL *= "https://api.pexels.com/v1/"
    
    private val *moshi *= Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    private val *retrofit *= Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(*moshi*))
        .baseUrl(*BASE_URL*)
        .build()
    
    interface PexelsApiService {
        @GET("search")
        @Headers("Authorization:563492ad6f917000010000011a5094093e6e4ee3978287979ad139ac")
        fun getData(@Query("query") keyword: String,
                    @Query("page") pageIndex: Int = 0,
                    @Query("per_page") perPage: Int = 20): Call<Data>
    }
    
    object PexelApi {
        val retrofitService: PexelsApiService by *lazy ***{
            ***retrofit*.create(PexelsApiService::class.*java*)
        **}
    **}

And we need to call this network API in the init block of the HomeViewModel class

Let’s add a new method getDataFromNetwork with following implementation into the HomeViewModel class

    private fun getDataFromNetwork() {
        PexelApi.retrofitService.getData(keyword = "panda").enqueue(
            object: Callback<Data> {
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    _data.*value *= response.body()
                }
    
                override fun onFailure(call: Call<Data>, t: Throwable) {
                    _data.*value *= Data(0, 0, *emptyList*(), 0, "")
                }
            }
        )
    }

and replace the getDataFromSample in the init block with new getDataFromNetwork function

And you would see lots of photo of panda, which is the hardcoded keyword now, displaying on the screen like this:

![](https://cdn-images-1.medium.com/max/2000/1*oR5AXNbcLuRlUmG8HlpOOQ.png)

As we saw, we have implemented the first version of network operation and integrated it into our app. It works with traditional Retrofit Callback, but let’s implement a coroutine version of same API in next chapter

### Coroutine version of network API

We all know the coroutine can make the Kotlin code more readable, so let’s try it.

Let’s add another method, getDataCoroutine into PexelsApiService as followed:

    @GET("search")
    @Headers("Authorization:563492ad6f917000010000011a5094093e6e4ee3978287979ad139ac")
    suspend fun getDataCoroutine(@Query("query") keyword: String,
                                 @Query("page") pageIndex: Int = 0,
                                 @Query("per_page") perPage: Int = 20): Data

It is a suspend function and returns Data object instead of Call<Data>.

And Let’s create another method, getDataFromNetworkCoroutine, with following implementation in HomeViewModel class

    private fun getDataFromNetworkCoroutine() {
        *viewModelScope*.*launch ***{
            **try {
                _data.*value *= PexelApi.retrofitService.getDataCoroutine(keyword = "panda")
            } catch (e: Exception) {
                _data.*value *= Data(0, 0, *emptyList*(), 0, "")
            }
        **}
    **}

and call it in the init block instead of getDataFromNetwork.

And we will get same result if we run the app again.

Now we have implemented the network operation with coroutine. it’s no magic, quite simple.

Now we have been able to fetch photos from Pexels API, but what would happen if network is unavailable or backend is down? So in next chapter, we will add more properties into the HomeViewModel class to handle the status of network.

## Add status into HomeViewModel

Let’s open HomeViewModel.kt and add an enum class PexelsApiStatus as followed:

    **enum class PexelsApiStatus { *LOADING*, *ERROR*, *DONE*}**
    
    class HomeViewModel : ViewModel() {
        ......

and add two new properties, response and status into HomeViewModel class as shown below:

    *// The internal MutableLiveData Data that stores the most recent data
    *private val _data = MutableLiveData<Data>()
    **private val _response = MutableLiveData<String>()
    private val _status = MutableLiveData<PexelsApiStatus>()
    **
    *// The external immutable LiveData for the response Data
    *val data: LiveData<Data>
        get() = _data
    
    **val response: LiveData<String>
        get() = _response
    
    val status: LiveData<PexelsApiStatus>
        get() = _status**

and update these two LiveData in getDataFromSample method like this:

    private fun getDataFromSample() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter = moshi.adapter(Data::class.*java*)
        val sampleData = jsonAdapter.fromJson(*sampleData*)
        _data.*value *= sampleData ?: Data(0, 0, *emptyList*(), 0, "")
    **    _response.*value *= "Success: init with sample data"
        _status.*value *= PexelsApiStatus.*DONE**
    *}

update the getDataFromNetwork as well

    private fun getDataFromNetwork() {
        val keyword = "panda"
        _status.*value *= PexelsApiStatus.*LOADING
        *PexelApi.retrofitService.getData(keyword = keyword).enqueue(
            object: Callback<Data> {
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    _data.*value *= response.body()
    **                _response.*value *= "Success: Pexel data about ${keyword} is fetched"
                    _status.*value *= PexelsApiStatus.*DONE
     **           *}
    
                override fun onFailure(call: Call<Data>, t: Throwable) {
                    _data.*value *= Data(0, 0, *emptyList*(), 0, "")
    **                _response.*value *= "Failure: ${t.message}"
                    _status.*value *= PexelsApiStatus.*ERROR
    **            *}
            }
        )
    }

and same update to getDataFromNetworkCoroutine

    private fun getDataFromNetworkCoroutine() {
        val keyword = "panda"
        _status.*value *= PexelsApiStatus.*LOADING
        viewModelScope*.*launch ***{
            **try {
                _data.*value *= PexelApi.retrofitService.getDataCoroutine(keyword = keyword)
    **            _response.*value *= "Success: Pexel data about ${keyword} is fetched"
                _status.*value *= PexelsApiStatus.*DONE
    **        *} catch (e: Exception) {
                _data.*value *= Data(0, 0, *emptyList*(), 0, "")
    **            _response.*value *= "Failure: ${e.message}"
                _status.*value *= PexelsApiStatus.*ERROR
    **        *}
        **}
    **}

We are using response to keep the error message if the network request is failed, and using status to save the status of the network request.

So we need to show users some difference if the network request is successful or failed.

The first step we need to do is adding another BindingAdapter, bindStatus into BindingAdapters.kt as followed:

    @BindingAdapter("marsApiStatus")
    fun bindStatus(statusImageView: ImageView,
                   status: PexelsApiStatus) {
        when (status) {
            PexelsApiStatus.*LOADING *-> {
                statusImageView.*visibility *= View.*VISIBLE
                *statusImageView.setImageResource(R.drawable.*loading_animation*)
            }
            PexelsApiStatus.*ERROR *-> {
                statusImageView.*visibility *= View.*VISIBLE
                *statusImageView.setImageResource(R.drawable.ic_connection_error)
            }
            PexelsApiStatus.*DONE *-> {
                statusImageView.*visibility *= View.*GONE
            *}
        }
    }

We need another drawable, ic_connection_error, you can create such a drawable with

    <vector xmlns:android="http://schemas.android.com/apk/res/android"
        android:width="100dp"
        android:height="100dp"
        android:tint="#A9A9AC"
        android:viewportWidth="24.0"
        android:viewportHeight="24.0">
        <path
            android:fillColor="#A9A9AC"
            android:pathData="M19.35,10.04C18.67,6.59 15.64,4 12,4c-1.48,0 -2.85,0.43 -4.01,1.17l1.46,1.46C10.21,6.23 11.08,6 12,6c3.04,0 5.5,2.46 5.5,5.5v0.5H19c1.66,0 3,1.34 3,3 0,1.13 -0.64,2.11 -1.56,2.62l1.45,1.45C23.16,18.16 24,16.68 24,15c0,-2.64 -2.05,-4.78 -4.65,-4.96zM3,5.27l2.75,2.74C2.56,8.15 0,10.77 0,14c0,3.31 2.69,6 6,6h11.73l2,2L21,20.73 4.27,4 3,5.27zM7.73,10l8,8H6c-2.21,0 -4,-1.79 -4,-4s1.79,-4 4,-4h1.73z" />
    </vector>

or download it somewhere like [material design](https://material.io/develop/android).

And then we have implemented the update of the status in HomeViewModel and bond it to the property pexelsApiStatus in the layout file, but where is the property? Let’s add it to fragment_home.xml

Let’s add following ImageView under the RecyclerView section

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".ui.home.HomeFragment">
    
        <androidx.recyclerview.widget.RecyclerView
        ......
        tools:listitem="@layout/grid_view_item" />
        
        <ImageView
            android:id="@+id/status_image"
            android:layout_width="200dp"
            android:layout_height="200dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:pexelsApiStatus="@{viewModel.status}" />
    
    </androidx.constraintlayout.widget.ConstraintLayout>

And then we can turn on the airplane mode and run the app.

It would display a loading spinner first, and the connection_error icon instead of photos like this:

![](https://cdn-images-1.medium.com/max/2000/1*oH5ZItnv8kPhU-eLjG9Vog.png)

![](https://cdn-images-1.medium.com/max/2000/1*03Aw27cSqF2mGj7CgLnJfg.png)

We have implemented all mandatory functions of this Photo App. And let’s consider what improvement we can do?

One improvement we can do is to add a persistent storage to cache the data which is previously fetched, and display this cached data when the app launches. And sending the network request at same time, when new data gets fetched, the app would refresh the list to display the new photos. In the case the network is unavailable, the app can still display cached data. It would be a better user experience.

## Add Persistent storage

We will introduce Room to save the data.
> The Room persistence library provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite. In particular, Room provides the following benefits:
> Compile-time verification of SQL queries.
> Convenience annotations that minimize repetitive and error-prone boilerplate code.
> Streamlined database migration paths.
> Because of these considerations, we highly recommend that you use Room instead of [using the SQLite APIs directly](https://developer.android.com/training/data-storage/sqlite).

([https://developer.android.com/training/data-storage/room](https://developer.android.com/training/data-storage/room))

Firstly, we need to add following dependencies into build.gradle(Project:Pexels)

    *// Room
    *implementation 'androidx.room:room-runtime:2.3.0-rc01'
    kapt 'androidx.room:room-compiler:2.3.0-rc01'

and Sync Now

Secondly, we need to create entity class. Let’s create a new package, database , in which all database related files are.

And we need to create a database entity class as followed:

    class DatabasePexelsPhoto constructor(
        @PrimaryKey
        val id: String,
        val width: Int = 0,
        val height: Int = 0,
        val url: String = "",
        val photographer: String = "",
        val photographerUrl: String = "",
        val photographerId: Int = 0,
        val avgColor: String = "",
        val src: String = "",
        val liked: Boolean = false)

And we will save photos only in the database, not the whole Data object, because it doesn’t make sense to save other properties likepage, perPage, totalResults and nextPage in database.

And we also need to implement a data conversion function because the types of fields in DatabasePexelsPhoto are different to them in Data.Photo. We need to convert the src from String in the DB to Data.Photo.Src object.

We got an error at line src = jsonAdapter.fromJson(it.src),. Just leave it, we will fix it later.

And we also need to implement another conversion function in Data.kt to convert the photo data from Data.Photo to DatabasePexelsPhoto as shown below:

    fun List<Data.Photo>.asDatabaseModel(): List<DatabasePexelsPhoto> {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter = moshi.adapter(Data.Photo.Src::class.*java*)
    
        return *map ***{
            **DatabasePexelsPhoto(
                id = **it**.id.toString(),
                width = **it**.width,
                height = **it**.height,
                url = **it**.url,
                photographer = **it**.photographer,
                photographerUrl = **it**.photographerUrl,
                photographerId = **it**.photographerId,
                avgColor = **it**.avgColor,
                src =jsonAdapter.toJson(**it**.src),
                liked = **it**.liked
            )
        **}
    **}

and update the line val src: Src, to val src: Src?, to fix previous error.

And then it’s time to create the DAO. Let’s create a new file, Room.kt in database package and declare an interface PexelsPhotoDao, with two methods, getPhotos and insertAll. When these methods are called, The Room would get / insert the data from / into database.

    @Dao
    interface PexelsPhotoDao {
        @Query("select * from databasepexelsphoto")
        fun getPhotos(): LiveData<List<DatabasePexelsPhoto>>
    
        @Insert(onConflict = OnConflictStrategy.*REPLACE*)
        fun insertAll(photos: List<DatabasePexelsPhoto>)
    }

And we need to create a Room instance, INSTANCE as followed, which is a singleton object across the whole app.

    @Database(entities = [DatabasePexelsPhoto::class], version = 1)
    abstract class PexelsPhotoDatabase: RoomDatabase() {
        abstract val pexelsPhotoDao: PexelsPhotoDao
    }
    
    private lateinit var *INSTANCE*: PexelsPhotoDatabase
    
    fun getDatabase(context: Context): PexelsPhotoDatabase {
        *synchronized*(PexelsPhotoDatabase::class.*java*) **{
            **if (!::*INSTANCE*.*isInitialized*) {
                *INSTANCE *= Room.databaseBuilder(context.*applicationContext*,
                    PexelsPhotoDatabase::class.*java*,
                    "pexelsphoto").build()
            }
        **}
        **return *INSTANCE
    *}

Now we have implemented an interface to get / put data from / into database with Room, but it is isolated with the network interface. We need to integrate them.

And according the [Guide to app architecture](https://developer.android.com/jetpack/guide), it suggests single source of truth, so we will fetch the data only from database. Even though new data fetched from network interface would be saved into database rather than be displayed directly. So we need to add a repository module.

Let’s create a new package, repository, and create a new repository class, PexelsPhotoReposiroty in this package. We also need to implement a property data, which reads data from database and wraps in a LiveData, and a method, in which a PexelApi call is made and saves the fetched data into database.

    class PexelsPhotoRepository(private val database: PexelsPhotoDatabase) {
    
        val data: LiveData<Data>
            get() {
                val photos = database.pexelsPhotoDao.getPhotos()
                return Transformations.map(photos) **{
                    **Data(
                        page = 0,
                        perPage = 0,
                        photos = **it**.*asDomainModel*(),
                        totalResults = **it**.size,
                        nextPage = ""
                    )
                **}
            **}
    
        suspend fun refreshPexelsPhoto(keyword: String) {
            withContext(Dispatchers.IO) **{
                **val data = PexelApi.retrofitService.getDataCoroutine(keyword)
                database.pexelsPhotoDao.insertAll(data.photos.*asDatabaseModel*())
            **}
        **}
    }

We are binding the LiveData<List<DatabasePexelsPhoto>> to the LiveData<Data> with the factory function Transformations.map(), which is observing the change of input LiveData, and updates the output LiveData accordingly.

And then we will modify the HomeViewModel class to let it fetch data from repository instead of network API.

We need to call getDatabse in the HomeViewModel to get the instance of the PexelsPhotoDatabse, and instantiate an object of PexelsPhotoRepository based on this database object. Let’s add following line at beginning of HomeViewModel class

    private val pexelsPhotoRepository = PexelsPhotoRepository(*getDatabase*(application))

You would notice we don’t have application instance so far, so we need to update the signature of the HomeViewModel class with

    class HomeViewModel : ViewModel()

to

    class HomeViewModel(application: Application) : AndroidViewModel(application)

and change the lines

    val data: LiveData<Data>
        get() = _data

to

    val data = pexelsPhotoRepository.data

It is as I mentioned before, we are fetching data from repository instead from backfield of data, which would be updated from network API directly.

It’s time to abandon the code in which we are fetching data from network API. We don’t need getDataFromNetwork and* *getDataFromNetworkCoroutine anymore.

But we need to implement another method refreshRepository as followed:

    private fun refreshRepository() {
        val keyword = "panda"
        _status.*value *= PexelsApiStatus.*LOADING
        viewModelScope*.*launch ***{
            **try {
                pexelsPhotoRepository.refreshPexelsPhoto(keyword)
                _response.*value *= "Success: Pexel data about ${keyword} is fetched"
                _status.*value *= PexelsApiStatus.*DONE
            *} catch (e: Exception) {
                _data.*value *= Data(0, 0, *emptyList*(), 0, "")
                _response.*value *= "Failure: ${e.message}"
                _status.*value *= PexelsApiStatus.*ERROR
            *}
        **}
    **}

and call it in the init block instead of calling getDataFromNetwork or* *getDataFromNetworkCoroutine

With this piece of code, we will send network request to refresh the data in the database when the app launches.

We also need to add an inner class Factory inside the HomeViewModel class as followed:

    class Factory(val app: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.*java*)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

This class would be used when we instantiate the HomeViewModel object in the HomeFragment class

We also need the final change in HomeFragment class to replace

    private val homeViewModel: HomeViewModel by lazy {
            ViewModelProvider(this)
                .get(HomeViewModel::class.java)
        }

to

    private val homeViewModel: HomeViewModel by lazy {
            val activity = requireNotNull( this.activity) {
                "You can only access the viewModel after onActivityCreated()"
            }
            ViewModelProvider(this, HomeViewModel.Factory(activity.application))
                .get(HomeViewModel::class.java)
        }

when we instantiate the HomeViewModel object.

Now we would notice the property PexelsPhotoRepository.data.get() would called to update HomeViewModel.data when the app launches and the method PexelsPhotoRepository.refreshPexelsPhoto() would be called afterwards to update the database.

Let’s run the app with network connection first. It would load the photos as usual like this:

![](https://cdn-images-1.medium.com/max/2000/1*8lIwAv8P3xjilzFS_HQupQ.png)

And the we kill the app and turn on the Airplane mode, launch the app again. You would find the data is read from the database would be displayed first, so the app would know the url of the photos, and then some cached photos by Retrofit could be displayed as followed 2 screenshots.

![](https://cdn-images-1.medium.com/max/2000/1*ww-cae00Ey2Q1mkFD7pcLA.png)

![](https://cdn-images-1.medium.com/max/2000/1*iMd25az0yqJOWLAk3ayTUA.png)

But if you scroll down, you would find some photos were keeping loading. It is because they were not cached by Retrofit although the url of the photos were correct.

![](https://cdn-images-1.medium.com/max/2000/1*TyqpRVNOwwiKDn5y-KT7xA.png)

Now we have completed the parts of fetching the data from network, saving the photos in the database, and displaying them in the RecyclerView with a single source of truth manner.

But you might notice that we fetched the data of 20 photos only once when the app launches, so it won’t fetch more photos. And we would also notice the Pexels API actually supports paging with a query parameter page, so in the next chapter, we would adding pagination to our app.

## Add Pagination

What we plan to do is to fetch next page of photos when the user scrolls to the bottom of the list.

Let’s openPexelsPhotoRespository.kt and add the backfield of the property data because we need to save nextPage url in the http response, but we don't want to save it in database. So modify the property data as followed:

    **val _data = MutableLiveData<Data>()
    **val data: LiveData<Data>
        get() {
            val photos = database.pexelsPhotoDao.getPhotos()
            return Transformations.map(photos) **{
                **Data(
    **                page = _data.*value*?.page ?: 0,
                    perPage = _data.*value*?.perPage ?: 0,
    **                photos = **it**.*asDomainModel*(),
                    totalResults = **it**.size,
    **                nextPage = _data.*value*?.nextPage ?: ""
    **            )
            **}
        **}

and we also need to modify the method refreshPexelsPhoto as shown below:

    suspend fun refreshPexelsPhoto(keyword: String, pageIndex: Int) {
        lateinit var data: Data
        withContext(Dispatchers.IO) **{
            **data = PexelApi.retrofitService.getDataCoroutine(keyword, pageIndex = pageIndex)
            database.pexelsPhotoDao.insertAll(data.photos.*asDatabaseModel*())
        **}
        **_data.*value *= data
    }

We made two changes, one is adding a new parameter, pageIndex for letting PexelApi know which page to fetch, another is to save the response data.

And then we need to update the HomeViewModel class as followed

Firstly, we need to change method refreshRepository to public, so it can be invoked from the fragment. And we also need to update this method as followed:

    **fun refreshRepository() {
    **    val keyword = "panda"
    **    val nextPageUri = data.*value*?.nextPage?.*toUri*()
        val pageIndex = nextPageUri?.getQueryParameter("page")?.*toInt*() ?: 0
    **    _status.*value *= PexelsApiStatus.*LOADING
        viewModelScope*.*launch ***{
            **try {
    **            pexelsPhotoRepository.refreshPexelsPhoto(keyword, pageIndex = pageIndex)**

                ......
        **}
    **}

Secondly, let’s update the PhotoGridAdapter class to do something when user scrolls to the bottom of the list.

We need to add a new parameters, onScrollToBottom, which is a function passed in, to the signature of PhotoGridAdapter as shown below:

    class PhotoGridAdapter**(private val onScrollToBottom: () -> Unit)**: ListAdapter<Data.Photo, PhotoGridAdapter.PhotoGridViewHolder>(DiffCallback)

and call this function onScrollToBottom, when scrolling to the bottom of the list in onBindViewHolder method as followed:

    override fun onBindViewHolder(holder: PhotoGridViewHolder, position: Int) {
        val photo = getItem(position)
    **    if (position == *itemCount *- 1) {
            onScrollToBottom()
        }
    **    holder.bind(photo)
    }

At last, let’s pass a function, homeViewModel.getDataFromRepository(), to PhotoGridAdapter object when it is instantiated in the HomeFragment class as shown below:

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
    
        *// Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        *binding.*lifecycleOwner *= this
    
        *// Giving the binding access to the OverviewViewModel
        *binding.*viewModel *= homeViewModel
    
    **    binding.photosGrid.*adapter *= PhotoGridAdapter() {homeViewModel.refreshRepository()}
    
        **return binding.*root
    *}

Let’s run the app and scroll down to the bottom of the list. We would see next page of photos would be loaded. And if you continue scrolling, more and more photos would be loaded.

![](https://cdn-images-1.medium.com/max/2000/1*zsyjeWM6Mjawh5-rKMrnrA.gif)

Now we have completed the pagination. It would fetch next page of data when scrolling to the bottom of current list. And also because all fetched photos would be added to database, so duplicated photos would be replaced.

So far so good. However, you might notice all photos are panda because the query keyword is hardcoded to panda. So in next chapter, we will add a SearchView to let user to input a keyword.

## Add SearchView

Firstly, we need to add a new API to the PexelsPhotoDao interface, clearAll, which is used to clean the database table when the keyword gets changed. Let’s open the Room.kt and add following method to PexelsPhotoDao interface.

    @Query("delete from databasepexelsphoto")
    fun clearAll()

And we also need to add a new method, clearDatabase, as followed into the PexelsPhotoRepository class.

    suspend fun clearDatabase() {
        withContext(Dispatchers.IO) **{
            **database.pexelsPhotoDao.clearAll()
        **}
    **}

It is a suspend function running on IO thread because database operation is long-running task as well.

And then we need to update the HomeViewModel. Let’s open HomeViewModel.kt and add a new parameter keyword into refreshRepository method as followed, and modify the implementation accordingly:

    fun refreshRepository(**keyword: String = ""**) {
    **    if (keyword.*isEmpty*()) {
            _status.*value *= PexelsApiStatus.*DONE
        *} else {
    **        val nextPageUri = data.*value*?.nextPage?.*toUri*()
            .....
            pexelsPhotoRepository.refreshPexelsPhoto(keyword, pageIndex = pageIndex)
            ......
           _status.*value *= PexelsApiStatus.*ERROR
        *}
    **}**

This change shows we would do nothing except setting the status to DONE if the keyword is empty, or else, we will use this keyword for fetching data while calling pexelsPhotoRepository.refreshPexelsPhoto method.

You might want to modify the methods getDataFromNetworkCoroutine and getDataFromNetwork with same change, although they are not in use anymore.

And then we need to add another private method clearDataFromDatabase as followed in HomeViewModel class

    private fun clearDataFromDatabase() {
        *viewModelScope*.*launch ***{
            **try {
                pexelsPhotoRepository.clearDatabase()
            } catch (e: Exception) {
                _response.*value *= "Failure: ${e.message}"
                _status.*value *= PexelsApiStatus.*DONE
            *}
        **}
    **}

this method would call pexelsPhotoRepository.clearDatabase() to clean the data in database.

And then we need to add a new method updateKeyword as followed, which would be invoked from HomeFragment when the keyword gets changed.

    fun updateKeyword( keyword: String) {
        clearDataFromDatabase()
        refreshRepository(keyword)
    }

This method is pretty straightforward, just cleaning the database, and fetching new data with new keyword.

Now we have updated the PexelsPhotoDao, PexelsPhotoRepository, HomeViewModel, it’s time to update the view, HomeFragment.

The first thing we need to do is to add a SearchView and correspondent listeners to HomeFragment class to allow user to input the keyword.

Let’s create a new xml file, home.xml in ./res/menu folder, and add one item in it as followed:

    *<?*xml version="1.0" encoding="utf-8"*?>
    *<menu xmlns:android="http://schemas.android.com/apk/res/android"
        **xmlns:app="http://schemas.android.com/apk/res-auto"**>
    **    <item android:id="@+id/action_search"
            android:title=""
            android:icon="@android:drawable/ic_menu_search"
            app:actionViewClass="androidx.appcompat.widget.SearchView"
            app:showAsAction="always"/>
    **</menu>

And then we need to open HomeFragment.kt and add a new interface androidx.appcompat.widget.SearchView.OnQueryTextListener to the HomeFragment class as followed:

    class HomeFragment : Fragment()**, androidx.appcompat.widget.SearchView.OnQueryTextListener**

and add a private property keyword to save the keyword that user inputed as followed.

    private var keyword = ""

and instantiate the SearchView and implement the listeners under the onCreateView method as shown below:

    private var searchView: androidx.appcompat.widget.SearchView? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.*home*, menu)
        searchView = menu.findItem(R.id.*action_search*)?.*let ***{
            it**.*actionView *as? androidx.appcompat.widget.SearchView
        **}**?.*apply ***{
            **(*activity*?.getSystemService(Context.*SEARCH_SERVICE*) as? SearchManager)?.*also ***{
                **setSearchableInfo(**it**.getSearchableInfo(*activity*?.*componentName*))
            **}
            ***maxWidth *= Int.MAX_VALUE
    
            setIconifiedByDefault(false)
            *isIconified *= false
            *isSubmitButtonEnabled *= false
            setOnQueryTextListener(this@HomeFragment)
            requestFocus()
            *layoutParams *= ActionBar.LayoutParams(
                ActionBar.LayoutParams.*MATCH_PARENT*,
                ActionBar.LayoutParams.*MATCH_PARENT*)
        **}
    **}
    
    override fun onQueryTextSubmit(query: String?): Boolean {
        return query?.*let ***{
            **if (**it**.length < 2) { return@let false }
            keyword = **it
            **homeViewModel.updateKeyword(keyword)
            searchView?.clearFocus()
            true
        **} **?: false
    }
    
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

In this piece of code, we would initialise a SearchView and bind it the the action_search in the home menu, which implement the search behaviour. We also implemented listeners onQueryTextSubmit, which would be called when the user clicks search icon on the keyboard, and trigger the homeViewModel.updateKeyword() method, and onQueryTextChange which indicates the input text is changed.

And we also need to add at beginning of the onCreateView method to show the menu as followed:

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
    **    setHasOptionsMenu(true)
    **    val binding = FragmentHomeBinding.inflate(inflater)
        ......*
    *}

Now let’s run the app, you will find a new SearchView at the ActionBar on the home page as followed screenshot. Then you can input the keyword, like gorilla, in the SearchView, and click search icon on the keyboard.

It would fetch photos with new keyword, gorilla, and update the database, and then refresh the list like this:

![](https://cdn-images-1.medium.com/max/2000/1*rtKx2RJLdVZBW9vPN2IyWQ.gif)

So far, the app is pretty good and fully functional. What other enhancement do you need? Maybe another page to display the details of a single photo. It will navigate to the new detail page when you clicks a single photo from the list. Let’s implement it in next chapter.

## Add Detail page

We will implement the navigation from Home page to Detail with [Android Navigation component](https://developer.android.com/guide/navigation), so we need to add following dependencies into dependencies section in the build.gradle(Module:Pexls.app)

    *// Navigation
    *implementation 'androidx.navigation:navigation-fragment-ktx:2.3.4'
    implementation 'androidx.navigation:navigation-ui-ktx:2.3.4'

And we will use a new feature, navigation-safe-args, so we need to add more plugin and dependencies

Let’s open build.gradle(Project:Pexels), and add following lines in the dependencies block as followed:

    dependencies **{
            **classpath "com.android.tools.build:gradle:4.1.1"
            classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
            **classpath "androidx.navigation:navigation-safe-args-gradle-plugin:2.3.4"**
    
            *// NOTE: Do not place your application dependencies here; they belong
            // in the individual module build.gradle files
        ***}
    }**

and open build.gradle(Module:Pexels.app) again to add another plugin into plugins section as shown below:

    plugins **{
        **id 'com.android.application'
        id 'kotlin-android'
        id 'kotlin-android-extensions'
        id 'kotlin-kapt'
        **id 'androidx.navigation.safeargs.kotlin'**
    **}**

Sync now

We will add ViewModel for the detail page in next step.

Let’s create a new package, detail in ui package first for sake of cleanness of the project.

And we need to create a new class, DetailViewModel, inside this package, and update the code as followed:

    class DetailViewModel(photo: Data.Photo, app: Application): AndroidViewModel(app) {
        private val _photo = MutableLiveData<Data.Photo>()
        val photo: LiveData<Data.Photo>
            get() = _photo
    
        init {
            _photo.*value *= photo
        }
    }

It’s pretty similar to HomeViewModel class as we just created a LiveData property, photo, in it to represent the photo to be displayed.

We also need to add a subclass of ViewModelProvider.Factory for checking the ViewModel would be created with correct type because we need to pass some parameter to it. Add the following code under the init block.

    class Factory( private val photo: Data.Photo,
                   private val app: Application): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.*java*)) {
                return DetailViewModel( photo, app) as T
            }
            throw IllegalArgumentException("Unknown View class")
        }
    
    }

Now, we have completed the ViewModel for detail page. It’s time to implement the View part.

Let’s create a new fragment, DetailFragment with the BlankFragment template. You would notice two new files, DetailFragment.kt and fragment_detail.xml, would be created.

Let’s update the fragment_detail.xml first.

We need to remove the whole FrameLayout section, and add a layout section as followed in order to use DataBinding.

    <layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools">
    </layout>

And we need to add a data block with variable block inside the layout block for declaring the name and type of the variable as shown below:

    <data>
        <variable
            name="viewModel"
            type="com.ovlesser.pexels.ui.detail.DetailViewModel" />
    </data>

And we also need to add some components under the data section as followed, image, id, size (width * height), url and photographer

    <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="16dp">
    
            <ImageView
                android:id="@+id/detail_photo"
                android:layout_width="0dp"
                android:layout_height="226dp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:imageUrl="@{viewModel.photo.src.medium}"
                tools:src="@tools:sample/backgrounds/scenic" />
            
            <TextView
                android:id="@+id/detail_id"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:textColor="#de000000"
                android:text="@{String.valueOf(viewModel.photo.id)}"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/detail_photo" />
    
            <TextView
                android:id="@+id/detail_size"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:textColor="#de000000"
                android:text="@{viewModel.size}"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/detail_id" />
    
            <TextView
                android:id="@+id/detail_url"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:textColor="#de000000"
                android:text="@{viewModel.photo.url}"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/detail_size" />
    
            <TextView
                android:id="@+id/detail_photographer"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:textColor="#de000000"
                android:text="@{viewModel.photo.photographer}"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/detail_url" />
    
        </androidx.constraintlayout.widget.ConstraintLayout>
    </ScrollView>

We put the ImageView to display the photo image, and some other TextView to display other info, in a ScrollView to make the whole layout scrollable just in case the image is too large.

We also set app:imageUrl="@{viewModel.photo.src.medium}" to set the source of the ImageView

You might notice an error highlighted at android:text=”@{viewModel.size}”, that is because there is no property or method called size in DetailViewModel class. To fix this issue we need to add a property size into DetailViewModel as followed:

    val size = Transformations.map(this.photo) **{
        **"${**it**.width} * ${**it**.height}"
    **}**

Then we would find the property DetailViewModel.size

Next step we would do is to update the data model because we need to pass argument to indicate which item is selected from the list at Home page to Detail page. And this argument must be serializable or parcelable, so we need to update Data, Data.Photo and Data.Photo.Src as followed:

    **@Parcelize**
    data class Data(
        val page: Int = 0,
        @Json(name = "per_page") val perPage: Int = 0,
        val photos: List<Photo>,
        @Json(name = "total_results") val totalResults: Int = 0,
        @Json(name = "next_page") val nextPage: String = ""
    )**: Parcelable** {
        **@Parcelize**
        data class Photo(
            val id: Int,
            val width: Int = 0,
            val height: Int = 0,
            val url: String = "",
            val photographer: String = "",
            @Json(name = "photographer_url") val photographerUrl: String = "",
            @Json(name = "photographer_id") val photographerId: Int = 0,
            @Json(name = "avg_color") val avgColor: String = "",
            val src: Src? = null,
            val liked: Boolean = false,
        )**: Parcelable** {
            **@Parcelize**
            data class Src(
                val original: String = "",
                val large2x: String = "",
                val large: String = "",
                val medium: String = "",
                val small: String = "",
                val portrait: String = "",
                val landscape: String = "",
                val tiny: String = ""
            )**: Parcelable**
        }
    }

We also added the default value to some propertied to simplify the instantiation of the Data object later.

Now it’s an interesting part that we will implement the navigation with a navigation graph.

Let’s open ./res/layout/navigation/mobile-navagation.xml file and add a new fragment section as shown below:

    <navigation xmlns:android="http://schemas.android.com/apk/res/android"
        ......
        app:startDestination="@+id/navigation_home">
    
        <fragment
           ......
           tools:layout="@layout/fragment_notifications" />
    
    **    <fragment
            android:id="@+id/navigation_detail"
            android:name="com.ovlesser.pexels.ui.detail.DetailFragment"
            android:label="@string/title_detail"
            tools:layout="@layout/fragment_detail" />
    **</navigation>

as a new navigation destination. We also add the string literal title_detail into ./res/values/string.xml as followed:

    <string name="title_detail">Detail</string>

Then Let’s add action into Home fragment as followed to makes us from Home fragment to Detail fragment

    <fragment
        android:id="@+id/navigation_home"
        android:name="com.ovlesser.pexels.ui.home.HomeFragment"
        android:label="@string/title_home"
        tools:layout="@layout/fragment_home"**>
        <action
            android:id="@+id/action_showDetail"
            app:destination="@id/navigation_detail" />
    </fragment>**

And we also need to add argument block into Detail fragment as followed to define the type of arguments which would be passed from Home fragment to Detail fragment.

    <fragment
        android:id="@+id/navigation_detail"
        android:name="com.ovlesser.pexels.ui.detail.DetailFragment"
        android:label="@string/title_detail"
        tools:layout="@layout/fragment_detail"**>
        <argument
            android:name="photo"
            app:argType="com.ovlesser.pexels.data.Data$Photo" />
    </fragment>**

Be careful the $ between Data and Photo because the Photo is an inner class of Data, and it is actually using reflection here.

After this step, you might need to close the project / clean the project to make sure class DetailFragmentArgs is generated it it’s not generated automatically.

Now, we need to update the DetailFragment class. Let’s open up the DetailFragment.kt and remove all template code and replace the onCreateView method with following code

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = *requireNotNull*(*activity*).*application
        *val binding = FragmentDetailBinding.inflate(inflater)
        binding.*lifecycleOwner *= this
    
        val photo = DetailFragmentArgs.fromBundle(requireArguments()).photo
        val viewModelFactory = DetailViewModel.Factory( photo, application)
        binding.*viewModel *= ViewModelProvider(
            this, viewModelFactory).get(DetailViewModel::class.*java*)
        return binding.*root
    *}

We would get a Photo object from requireArguments() when the DetailFragment gets initialised.

And then we need to update the PhotoGridAdapter class to make it responds the click event on list item.

Let’s open PhotoGridAdapter.kt and add a new property, onClickListener, to the PhotoGridAdapter class as followed:

    class PhotoGridAdapter(**private val onClickListener: OnClickListener,**
                           private val onScrollToBottom: () -> Unit): ListAdapter<Data.Photo, PhotoGridAdapter.PhotoGridViewHolder>(DiffCallback)

And we also need to implement a inner class OnClickListener as shown below:

    class OnClickListener(val clickListener: (photo: Data.Photo) -> Unit) {
        fun onClick( photo: Data.Photo) = clickListener( photo)
    }

and add the response of click event to the onBindViewHolder method as followed:

    override fun onBindViewHolder(holder: PhotoGridViewHolder, position: Int) {
        val photo = getItem(position)
        if (position == *itemCount *- 1) {
            onScrollToBottom()
        }
    **    holder.itemView.setOnClickListener {
            onClickListener.onClick(photo)
        }
        **holder.bind(photo)
    }

Now, it would call the callback function onClickListener.onClick(photo) when a list item gets clicked. This callback function is passed from HomeFragment when the PhotoGridAdapter object is instantiated.

Nest step is to update the HomeViewModel class because we need to save which list item gets clicked.

Let’s open HomeViewModel.kt file and add another property selectedPhoto and its backfield _selectedPhoto as shown below:

    private val _status = MutableLiveData<PexelsApiStatus>()
    **private val _selectedPhoto = MutableLiveData<Data.Photo?>()
    **......
    val status: LiveData<PexelsApiStatus>
        get() = _status
    
    **val selectedPhoto: LiveData<Data.Photo?>
        get() = _selectedPhoto**

And we also need to add two methods displayPhotoDetails(photo: Data.Photo) and displayPhotoDetailComplete() as followed, in which the property selectedPhoto would be updated.

    fun displayPhotoDetails( photo: Data.Photo) {
        _selectedPhoto.*value *= photo
    }
    
    fun displayPhotoDetailComplete() {
        _selectedPhoto.*value *= null
    }

Now, let’s update the HomeFragment class to bind all changes together.

Let’s open HomeFragment.kt and updated the instantiation of viewModelAdapter as followed:

    binding.photosGrid.*adapter *= PhotoGridAdapter(** PhotoGridAdapter.OnClickListener {
        homeViewModel.displayPhotoDetails(it)
    }**) **{**homeViewModel.refreshRepository()**}**

With this change, we would pass a lambda function PhotoGridAdapter.OnClickListener {
 homeViewModel.displayPhotoDetails(it)
} into the photoGridAdapter object. And the onClick() would set to homeViewModel.displayPhotoDetails(it). It mean homeViewModel.displayPhotoDetails(it) would be called when the user clicks a list item. The parameter would be the photo object of the selected list item.

And we also need to add an observer as followed to observe the LiveData property selectedPhoto and navigate to the destination fragment, DetailFragment if selectedPhoto is changed.

    homeViewModel.selectedPhoto.observe(*viewLifecycleOwner*, *Observer ***{
        **if (**it **!= null) {
            this.*findNavController*().navigate(
                HomeFragmentDirections.actionShowDetail(it))
            homeViewModel.displayPhotoDetailComplete()
        }
    **}**)

You might need to restart the Android Studio if you found findNavController cannot be found.

We are almost done. Let’s run the app and click a photo from the list on home page, it would navigate to the detail page to show this photo with some info. It’s just what we expected. But you might notice a minor issue that you are unable to go back to home page by clicking Back button on detail page. Let’s fix it.

We need to open MainActivity.kt and add following override method to enable the back button.

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.*itemId*) {
            android.R.id.*home *-> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

Then let’s run the app again. It would display home page. And it will navigate to the detail page once you click a single photo, and go back to home page if you click Back button on detail page like this:

![](https://cdn-images-1.medium.com/max/2000/1*3Ov8eb9Jgd7ESlCRbbi1kA.gif)

Then the app would be able to go back to previous page just like pressing the Back button once the user clicks the Back button at the ActionBar. It won't impact navigating between home, dashboard and notification pages because these pages have been set as top level pages with following template code in the onCreate method.

    val appBarConfiguration = *AppBarConfiguration*(*setOf*(
            R.id.*navigation_home*, R.id.*navigation_dashboard*, R.id.*navigation_notifications*))
    *setupActionBarWithNavController*(navController, appBarConfiguration)

We have completed all function of this photo app for now. In future tutorials, we might add more feature to fill up other pages.

Thanks for browsing my tutorial and any feedbacks are welcome.
