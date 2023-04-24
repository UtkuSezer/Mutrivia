import 'package:flutter/material.dart';

class OptionsScreen extends StatelessWidget {
  const OptionsScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Row(
          mainAxisAlignment: MainAxisAlignment.center, // Center Row contents horizontally
          children: <Widget>[
            Image.asset('assets/images/favicon_white.png', height: 40, width: 40,),
            const Text('Mutrivia'),
          ],
        ),
        centerTitle: true,
        backgroundColor: Colors.deepPurple[500],
      ),

      body: Center(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                textStyle: const TextStyle(fontSize: 20),
                primary: Colors.deepPurple[700],
                onPrimary: Colors.white,
                onSurface: Colors.grey,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
              ),
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) =>
                        const OptionsScreen()));
              }, //burda host olma, ya da odaya katılma veya tek oynama gibi seçenekleri seçeceği sayfaya gitmeli basınca yani on pressed
              child: const Text('i am so lonely'),
            ),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                textStyle: const TextStyle(fontSize: 20),
                primary: Colors.deepPurple[00],
                onPrimary: Colors.white,
                onSurface: Colors.grey,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
              ),
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) =>
                        const OptionsScreen()));
              }, //burda host olma, ya da odaya katılma veya tek oynama gibi seçenekleri seçeceği sayfaya gitmeli basınca yani on pressed
              child: const Text('i am with a KABİLE'),
            ),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                textStyle: const TextStyle(fontSize: 20),
                primary: Colors.deepPurple[100],
                onPrimary: Colors.white,
                onSurface: Colors.grey,
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
              ),
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) =>
                        const OptionsScreen()));
              }, //burda host olma, ya da odaya katılma veya tek oynama gibi seçenekleri seçeceği sayfaya gitmeli basınca yani on pressed
              child: const Text('i am the HOST'),
            ),
          ],
        ),
      ),

    );
  }
}