package com.example.autocars.db.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson


class CarMedia() : Parcelable {
    var id: Int= 0
    var name: String? = ""
    var url: String? = ""
    var createdAt: String? = ""
    var type: String? = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        url = parcel.readString()
        createdAt = parcel.readString()
        type = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<CarMedia> {
        override fun createFromParcel(parcel: Parcel): CarMedia {
            return CarMedia(parcel)
        }

        override fun newArray(size: Int): Array<CarMedia?> {
            return arrayOfNulls(size)
        }
    }
}

        data class CarMediaList(
        val carMediaList: List<CarMedia>,
        val pagination: Pagination
)

data class PopularCarsResponse(
        val makeList: List<PopularMakes>,
        val pagination : Pagination
)

 class PopularMakes {
        var id: String? = ""
        var name: String? = ""
        var imageUrl: String? = ""
 }

data class Pagination(
        val total : Int,
        val currentPage: Int,
        val pageSize: Int

)

data class PopularListSearch(
        val result: List<PopularList>,
        val pagination : Pagination
)

@Entity
data class Favorites(
    @PrimaryKey(autoGenerate = false)
    val id: String ,
    val title: String,
    val imageUrl: String,
    val year: String,
    val city: String,
    val state: String,
    val marketplacePrice: Double
)

data class Messanger( val id: String? ,val imageUrl: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Messanger> {
        override fun createFromParcel(parcel: Parcel): Messanger {
            return Messanger(parcel)
        }

        override fun newArray(size: Int): Array<Messanger?> {
            return arrayOfNulls(size)
        }
    }
}

data class PopularList (

        val id: String? ,
        val title: String?,
        val imageUrl: String?,
        val year: String?,
        val city: String?,
        val state: String?,

        val gradeScore: Double,
        val sellingCondition: String?,
        val hasWarranty: Boolean,

        val marketplacePrice: Double,
        val marketplaceOldPrice: Double,
        val hasFinancing: Boolean,

        val mileage: Double,
        val mileageUnit: String?,
        val installment: String?,
        val depositReceived: Boolean,
        val loanValue: Double,
        val websiteUrl: String?,
        //@TypeConverters(SortCodeConverter::class)
        val stats : Stats,
        val bodyTypeId:  String?,
        val sold: Boolean,
        val hasThreeDImage: Boolean,
        var salFavorite : Boolean? = false

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readString(),
        TODO("stats"),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<PopularList> {
        override fun createFromParcel(parcel: Parcel): PopularList {
            return PopularList(parcel)
        }

        override fun newArray(size: Int): Array<PopularList?> {
            return arrayOfNulls(size)
        }
    }
}


class SortCodeConverter {
    @TypeConverter
    fun toString(bankSortCodeEntity: Stats?): String {
        return Gson().toJson(bankSortCodeEntity)
    }

    @TypeConverter
    fun toBankSortCode(str: String?): Stats {
        return Gson().fromJson(str, Stats::class.java)
    }
}

class Stats{
        val webViewCount: Double? = 0.toDouble()
        val webViewerCount: Double? = 0.toDouble()
        val interestCount: Double? = 0.toDouble()
        val testDriveCount: Double? = 0.toDouble()
        val appViewCount: Double? = 0.toDouble()
        val appViewerCount: Double? = 0.toDouble()
        val processedLoanCount: Double? = 0.toDouble()
}

class  CarDetails() : Parcelable {
    var features : List<Features>? = emptyList()
    var carFeatures : List<CarFeatures> = emptyList()
    var modelFeatures : List<ModelFeatures> = emptyList()
    var damageMedia :  List<DamageMedia>  = emptyList()
    var id: String? = ""

    var year: Int = 0
    var insured: Boolean = false
    var mileage: Int? = 0
    var vin: String? = ""

    var licensePlate: String? = ""
    var engineNumber: String? =  ""
    var price: Double? = 0.toDouble()
    var createdBy: String? = ""
    var marketplacePrice: Double? = 0.toDouble()
    var marketplaceVisible: Boolean? = false
    var marketplaceVisibleDate: String? = ""
    var isFeatured: Boolean? = false
    var reasonForSelling: String? = ""
    var imageUrl: String? = ""
    var ownerId: String? = ""

    var model : Model ? = null
    var state: String? = ""
    var country: String? = ""
    var address: String? = ""
    var carManagerId: String? = ""
    var ownerType: String? = ""
    var transmission: String? = ""
    var fuelType:String? = ""
    var sellingCondition: String? = ""
    var bodyType = PopularMakes()
    var city: String? = ""
    var marketplaceOldPrice: String? = ""
    var createdAt:String? = ""
    var updatedAt:String? = ""
    var mileageUnit: String? = ""
    var hasWarranty: Boolean? = false
    var hasFinancing: Boolean? = false
    var interiorColor: String? = ""
    var exteriorColor:String? = ""
    var engineType:String? = ""
    var gradeScore: Double? = 0.toDouble()
    var installment: Double? = 0.toDouble()
    var depositReceived: Boolean? = false
    var isFirstOwner: Boolean? = false
    var firstOwnerName:String? = ""
    var firstOwnerPhone: String? = ""
    var loanValue: Double? = 0.toDouble()
    var websiteUrl:String? = ""
    var stats = Stats()
    var sold: Boolean? = false
    var hasThreeDImage: Boolean? = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        year = parcel.readInt()
        insured = parcel.readByte() != 0.toByte()
        mileage = parcel.readValue(Int::class.java.classLoader) as? Int
        vin = parcel.readString()
        licensePlate = parcel.readString()
        engineNumber = parcel.readString()
        price = parcel.readValue(Double::class.java.classLoader) as? Double
        createdBy = parcel.readString()
        marketplacePrice = parcel.readValue(Double::class.java.classLoader) as? Double
        marketplaceVisible = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        marketplaceVisibleDate = parcel.readString()
        isFeatured = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        reasonForSelling = parcel.readString()
        imageUrl = parcel.readString()
        ownerId = parcel.readString()
        state = parcel.readString()
        country = parcel.readString()
        address = parcel.readString()
        carManagerId = parcel.readString()
        ownerType = parcel.readString()
        transmission = parcel.readString()
        fuelType = parcel.readString()
        sellingCondition = parcel.readString()
        city = parcel.readString()
        marketplaceOldPrice = parcel.readString()
        createdAt = parcel.readString()
        updatedAt = parcel.readString()
        mileageUnit = parcel.readString()
        hasWarranty = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        hasFinancing = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        interiorColor = parcel.readString()
        exteriorColor = parcel.readString()
        engineType = parcel.readString()
        gradeScore = parcel.readValue(Double::class.java.classLoader) as? Double
        installment = parcel.readValue(Double::class.java.classLoader) as? Double
        depositReceived = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        isFirstOwner = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        firstOwnerName = parcel.readString()
        firstOwnerPhone = parcel.readString()
        loanValue = parcel.readValue(Double::class.java.classLoader) as? Double
        websiteUrl = parcel.readString()
        sold = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        hasThreeDImage = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<CarDetails> {
        override fun createFromParcel(parcel: Parcel): CarDetails {
            return CarDetails(parcel)
        }

        override fun newArray(size: Int): Array<CarDetails?> {
            return arrayOfNulls(size)
        }
    }
}



class Features()
class CarFeatures()
class ModelFeatures()
class DamageMedia{
    val inspectionItems : List<InspectionItems>? = emptyList()
    val name: String? = ""
    val comment:String? = ""
}

class Medias{}

class InspectionItems{
        val medias: List<Medias>? = null
        val name : String? = ""
        val response : String? = ""
}

data class  Model (
    var modelFeatures : List<ModelFeatures>,
    val id: Int,
    val name: String,
    val imageUrl: String ,
    val wheelType: String,
    val series: String? = null,
    val make :  PopularMakes,
    val popular: Boolean

)

