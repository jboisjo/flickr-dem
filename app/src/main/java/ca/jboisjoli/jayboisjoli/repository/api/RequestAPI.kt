package ca.jboisjoli.jayboisjoli.repository.api

import ca.jboisjoli.jayboisjoli.repository.model.entity.Detail
import ca.jboisjoli.jayboisjoli.repository.model.entity.Photo
import ca.jboisjoli.jayboisjoli.repository.model.entity.Photos
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.net.URL


internal class RequestAPI {

    private var apiKey = "abbe789cf545bf87978f2893d0f50011"
    private var apiUrl = "https://api.flickr.com/services/rest"

    private var apiMethodSearch = "flickr.photos.search"
    private var apiGetInfo = "flickr.photos.getInfo"

    private var formatUrl = "json"
    private var jsonCallback = "1"

    private lateinit var url: String
    private lateinit var listPhoto: MutableList<Photo>

    fun getPhotos(page: Int, searchQuery: String): Photos {

          url = apiUrl +
                "?method=$apiMethodSearch" +
                "&api_key=$apiKey" +
                "&page=$page" +
                "&tags=$searchQuery" +
                "&format=$formatUrl" +
                "&nojsoncallback=$jsonCallback"

        val repoListJsonStr = URL(url).readText()
        val mutableList: MutableList<Photo> = mutableListOf()

        // main jsonObject(Photos)
        val rootJsonObject = JSONObject(repoListJsonStr)
        val photos = rootJsonObject.getString("photos")

        // get data from jsonObject (Photos)
        val rootPhotoJsonObject = JSONObject(photos)
        val page = rootPhotoJsonObject.getString("page").toInt()
        val pages = rootPhotoJsonObject.getString("pages").toInt()
        val perpage = rootPhotoJsonObject.getString("perpage").toInt()
        val total = rootPhotoJsonObject.getString("total")

        val photoArray = rootPhotoJsonObject.getString("photo")
        val photoJsonArray = JSONArray(photoArray)

        // loop inside Photo Array
        for (i in 0 until photoJsonArray.length()) {
            val photoIndex = photoJsonArray.get(i).toString()
            val photoIndexObject = JSONObject(photoIndex)

            val id = photoIndexObject.getString("id")
            val owner = photoIndexObject.getString("owner")
            val secret = photoIndexObject.getString("secret")
            val server = photoIndexObject.getString("server")
            val farm = photoIndexObject.getString("farm").toInt()
            val title = photoIndexObject.getString("title")
            val ispublic = photoIndexObject.getString("ispublic").toInt()
            val isfriend = photoIndexObject.getString("isfriend").toInt()
            val isfamily = photoIndexObject.getString("isfamily").toInt()

            val photoObject = Photo(id, owner, secret, server, farm, title, ispublic, isfriend,
                    isfamily)

            mutableList.add(i, photoObject)
        }

        listPhoto = mutableList

        Timber.d("RequestAPI after" + listPhoto.size)

        return Photos(page, pages, perpage, total, listPhoto)
    }

    fun getPhotoDetail(id: String): Detail {

          url = apiUrl +
                "?method=$apiGetInfo" +
                "&api_key=$apiKey" +
                "&photo_id=$id" +
                "&format=$formatUrl" +
                "&nojsoncallback=$jsonCallback"

        val repoListJsonStr = URL(url).readText()

        //photo root object
        val rootJsonObject = JSONObject(repoListJsonStr)
        val photos = rootJsonObject.getString("photo")

        //title root object
        val titleJsonObject = JSONObject(photos)
        val title = titleJsonObject.getString("title")

        //get the title content element in description
        val titleContent = JSONObject(title)
        val contentTitle = titleContent.getString("_content")

        //description root object
        val photoJsonObject = JSONObject(photos)
        val description = photoJsonObject.getString("description")

        //get the description content element in description
        val descriptionObject = JSONObject(description)
        val contentDescription = descriptionObject.getString("_content")

        //owner root object
        val ownerObject = JSONObject(photos)
        val owner = ownerObject.getString("owner")

        //get the content element in description
        val getUsername = JSONObject(owner)
        val username = getUsername.getString("username")

        return Detail(contentTitle, contentDescription, username)
    }


    //TODO this section is to authenticate but we need the private key in order to do so.
    /*private fun oauthEncode(input: String): String {
        val oathEncodeMap = hashMapOf<String, String>()
        oathEncodeMap["\\*"] = "%2A"
        oathEncodeMap["\\+"] = "%20"
        oathEncodeMap["%7E"] = "~"
        var encoded = ""
        try {
            encoded = URLEncoder.encode(input, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        for (entry in oathEncodeMap.entries) {
            encoded = encoded.replace(entry.key.toRegex(), entry.value)
        }
        return encoded
    }

    var nonce = "flickr_oauth" + System.currentTimeMillis().toString()
    var timestamp = (System.currentTimeMillis() / 1000).toString()
    var callbackParam = "oauth_callback=" + oauthEncode("some_ callback_url}")
    var apiKeyParam = "oauth_consumer_key=$apiKey" //your apiKey from flickr
    var nonceParam = "oauth_nonce=$nonce"
    var signatureMethodParam = "oauth_signature_method=" + "HMAC-SHA1"
    var timestampParam = "oauth_timestamp=$timestamp"
    var versionParam = "oauth_version=" + "1.0"
    var unencBaseString3 = "$callbackParam&$apiKeyParam&$nonceParam&$signatureMethodParam&$timestampParam&$versionParam"
    var baseString3 = oauthEncode(unencBaseString3)
    var secretKey = "6727a7af675534fe + &"
    val oauthSignatureKey = secretKey

    val baseString1 = "GET"
    var requestTokenUrl = "http://www.flickr.com/services/oauth/request_token"
    var baseString2 = oauthEncode(requestTokenUrl)

    var baseString = "$baseString1&$baseString2&$baseString3"

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSignature(key: String, data: String): String {
        val HMAC_ALGORITHM = "HmacSHA1"
        val keySpec = SecretKeySpec(key.toByteArray(), HMAC_ALGORITHM)
        var macInstance: Mac? = null
        try {
            macInstance = Mac.getInstance(HMAC_ALGORITHM)
            macInstance!!.init(keySpec)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }

        val signedBytes = macInstance!!.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(signedBytes)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showSignature() {
        var signature = getSignature(oauthSignatureKey, baseString)
        var signatureParam = "oauth_signature=" + oauthEncode(signature)
        var urlToken = "$requestTokenUrl?$callbackParam&$apiKeyParam&$nonceParam&$timestampParam&$signatureMethodParam&$versionParam&$signatureParam"

        Log.e("tag", urlToken)
    }*/

}