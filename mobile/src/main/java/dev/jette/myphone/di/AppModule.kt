package dev.jette.myphone.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

//import dev.jette.myphone.demo.UserRepository
//import dev.jette.myphone.demo.UserRepositoryImpl
//import org.koin.core.module.dsl.bind
//import org.koin.core.module.dsl.factoryOf
//import org.koin.core.module.dsl.singleOf
//import org.koin.core.module.dsl.viewModelOf
//import org.koin.dsl.module
//
//val appModule = module {
////    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
////    factoryOf(::UserPresenter)
////    viewModelOf(::UserViewModel)
//}

@Module
@ComponentScan("dev.jette.myphone")
class AppModule
