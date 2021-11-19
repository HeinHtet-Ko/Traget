package com.mtu.ceit.hhk.traget.ui.setting

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.mtu.ceit.hhk.traget.data.model.Client
import com.mtu.ceit.hhk.traget.data.model.Diesel
import com.mtu.ceit.hhk.traget.data.model.Maintenance
import com.mtu.ceit.hhk.traget.util.Constants
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.io.File

class CSV_Writer {

    companion object {
        suspend fun <T>writeCSV(csvFile: File,rowList:List<T>){



            csvWriter().openAsync(csvFile,append = false){


                    rowList.forEach { row ->

                        when(row) {
                            is Client -> {
                                Timber.tag("bugmebro").e(rowList.size.toString()+"clients")
                                val (id,bId,name,time,amt,isPaid,macType,note,date) = row
                                val clientRow = listOf(id,bId,name,time,amt,isPaid,macType,note,date)
                                writeRow(clientRow)
                            }

                            is Maintenance -> {
                                Timber.tag("bugmebro").e(rowList.size.toString()+"maintains")
                                val (id,name,price,date) = row
                                val maintainRow = listOf(id,name,price,date)
                                writeRow(maintainRow)
                            }
                            is Diesel -> {
                                Timber.tag("bugmebro").e(rowList.size.toString()+"diesels")
                                val (bid,price,isActive,date) = row
                                val dieselRow = listOf(bid,price,isActive,date)
                                writeRow(dieselRow)
                            }

                        }

                    }



            }

        }


         suspend fun readClientCSV(csvFile: File, lambda:  (Client) -> Unit){


            var id:Int = 0
            var bID:Int = 0
            var name = ""
            var time:Int = 0
            var amt:Int = 0
            var isPaid = false
            var macType = Constants.ROTAVATOR
            var note:String? = null
            var date:Long = 0L



            csvReader().openAsync(csvFile){

                    readAllAsSequence().asFlow().collect {

                        it.forEachIndexed { index, s ->
                            when(index) {
                                0-> {
                                    id = s.toInt()
                                }
                                1 -> {
                                    bID = s.toInt()
                                }
                                2 -> {
                                    name = s
                                }
                                3 -> {
                                    time = s.toInt()
                                }
                                4 -> {
                                    amt = s.toInt()
                                }
                                5 -> {
                                    isPaid = s.toBoolean()
                                }
                                6 -> {
                                    macType =s
                                }
                                7 -> {
                                    note = s
                                }
                                8 -> {
                                    date = s.toLong()
                                }
                            }
                        }

                        lambda.invoke(Client(id,bID,name,time,amt,isPaid,macType,note,date))

                    }

                Timber.tag("readeo").e("readclientdone ${Thread.currentThread().name}")
                }

        }

        suspend fun readMaintainCSV(csvFile: File,lambda: (Maintenance) -> Unit) {

            var id:Int = 0
            var name:String = ""
            var price:Int = 0
            var date:Long = 0L

            csvReader().openAsync(csvFile){

                readAllAsSequence().asFlow().collect {

                    it.forEachIndexed { index, s ->
                        when(index) {
                            0 -> {
                                id = s.toInt()
                            }
                            1 -> {
                                name = s
                            }
                            2 -> {
                                price = s.toInt()
                            }
                            3 -> {
                                date = s.toLong()
                            }
                        }

                    }

                    lambda.invoke(Maintenance(id,name,price, date))
                }

                Timber.tag("readeo").e("readmaintaindone ${Thread.currentThread().name}")

            }

        }

        suspend fun readDieselCSV(csvFile: File,lambda:(Diesel) -> Unit) {
            var bID:Int = 0
            var price:Int = 0
            var isActive:Boolean = false
            var date:Long = 0L

            csvReader().openAsync(csvFile){
                readAllAsSequence().asFlow().collect {

                    it.forEachIndexed{index, s ->
                        when(index) {
                            0 -> {
                               bID =  s.toInt()
                            }
                            1 -> {
                                price = s.toInt()
                            }
                            2 -> {
                                isActive = s.toBoolean()
                            }
                            3 -> {
                                date = s.toLong()
                            }

                        }

                    }

                    lambda.invoke(Diesel(bID,price,isActive, date))

                }

                Timber.tag("readeo").e("readdieseldone ${Thread.currentThread().name}")
            }

        }
    }


}