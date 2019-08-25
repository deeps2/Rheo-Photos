# Rheo-Photos <img src="app/src/main/res/mipmap-hdpi/ic_launcher.png" />
Image search app inspired by Pinterest using MVP pattern and Room ORM.<br/>
APK: https://drive.google.com/open?id=1c3wR2GeHDkFVRcP-95Nj_hbmJummhtOx

# ScreenShots

<img src="https://res.cloudinary.com/deeps2/image/upload/v1566728961/rheo_photos/search_results.jpg" width=280>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://res.cloudinary.com/deeps2/image/upload/v1566728959/rheo_photos/pagination_request_ongoing_with_placeholder.jpg" width=280/></br></br></br></br>
<img src="https://res.cloudinary.com/deeps2/image/upload/v1566728957/rheo_photos/no_net.png" width=280/>
<img src="https://res.cloudinary.com/deeps2/image/upload/v1566728958/rheo_photos/no_results.png" width=280/>&nbsp;&nbsp; 
<img src="https://res.cloudinary.com/deeps2/image/upload/v1566728957/rheo_photos/generic_error.png" width=280/>&nbsp;&nbsp;


# Features
- Search images by entering the keywork (uses Microsoft Azure Congitive Services Bing Image Search API)
- Supports pagination (fetch results in group of 20)
- Stores previous search results into SQLite using Room ORM. 
- Thus if a search result exist it can be fetched from local DB. No need of network connection in this case.
- After MAX_ROWS limit is reached, oldest record will be deleted. Otherwise App's cache size will go on increasing.
- API keys are stored natively (JNI) rather than hardcoding it in a constant in java file.
- Images are perfectly fitted into ImageViews according to their aspect ratio. So that they will not be cropped.

# Components Used
- MVP pattern
- Room ORM is used for caching of search results
- Retrofit, Gson, Glide, Butterknife, Android Debug Db(for debugging), AndroidX(Appcompat & Material design)
- ConstraintLayout, RecyclerView, CardView
- Bing Image Search API https://docs.microsoft.com/en-us/azure/cognitive-services/bing-image-search/overview
