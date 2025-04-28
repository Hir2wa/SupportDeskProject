                       RMI
                    ===========

RMI stands for remote method invocation where is technology used in java to develop a sistrivution java applicatiion. i.e we haave client and sever voth are able to commnicatr vise versa .

IN RMI inorder sever and cliesnt to communicate they have to yse Gatewat namely STUB on client and SKeleton on server side /

A.initiate communicate with server
b.If server is alive it has to invoke methid exposed tby the setver
3.it has to wait feedvack fro the setvert
4.it communicate tge feedvac to the client

b.
it alllows/ reject incomiing request from thghe cliesnt
2.it has to execute the in coked methid bt the clinet
3.it has to commucate the feedcak to the client

c.steps to confugure the cliesnt siede

1,.create the pojo classsess without java persistance annotation . and it has to implemene t serializable
2.Each pojo interface class ehich ezpose the methos has to be invoked by the cliesnt .
3.presentation layer of the software should he implented on the client side .
i.e: view packafe witbswing GUI forms should be available here N.B

a.each pojo should be uder model packeafe
b.Each java Interfacce class should be iunder service package
c. Each java interface extends remote.
d.Each methods under java Interface class shoukd throws remoteException.
e. Import requirred libraries.

D.Steps to configure servet side

1.improt require libraries
2.creatrea pojon classes u dr model packafe, adn eachb pojo class should implement serializacle and salso have java persistance annotations e.g @entity.

3.Each pojo class should have its own dai class which holds all crud operarion . 4. All Hibernates configuration are implemented on the server side
5.EAch POJO class should have its own hava in tercace class which extends remote where ist exposes the methods to invoked by the client
6.Each kava interfcae classshoukd have its own java implementation class where uniquee remote method i terfcae class
7.the sercet side should have one main entru i.e server controller class

N.b
a.Each pojo classes should be undr mofa package

b. Each java interface class should be under service package
c. Each methods exposed under have interface classmyst rhriws rmote exception
d. Each java impleantion package
