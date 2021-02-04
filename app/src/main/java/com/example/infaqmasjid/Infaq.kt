package com.example.infaqmasjid

class Infaq {

    var id: Int? = null
    var name: String? = null
    var jumlah: String? = null
    var alamat: String? = null

    constructor(id: Int, name: String, jumlah: String, alamat: String) {
        this.id = id
        this.name = name
        this.jumlah = jumlah
        this.alamat = alamat
    }

}