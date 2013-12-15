using UnityEngine;
using System.Collections;
using Parse;

public class ParseDataInput : MonoBehaviour {

    private float fPastTime;
    private Color currColor;
	// Use this for initialization
	void Start () 
    {
        //ParseObject testObject = new ParseObject("TestObject");
        //testObject["foo"] = "bar";
        //testObject.SaveAsync();
        Debug.Log("Parse data init complete.");

        //currColor = this.gameObject.transform.renderer.material.color;
        //currColor.r = 125;
        //currColor.g = 125;
        //currColor.b = 125;
	}
	
	// Update is called once per frame
	void Update ()
    {
        fPastTime += Time.deltaTime;
        //Debug.Log(fPastTime.ToString());


        
	}
}
