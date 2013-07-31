using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using BarChart;
using System.Collections;

namespace WindowsApplication1
{
    public partial class Form1 : Form
    {
        private HBarChart barChart;
        private Color[] barColor;
        private const int P_COUNT = 10;

        Random rand = new Random();
        int nCroodX;
        int nCroodY;
        ArrayList arrRes = new ArrayList();
        ArrayList arrNextHourPrices = new ArrayList();
        ArrayList arrFinalPrices = new ArrayList();
        ArrayList arrInventory = new ArrayList();

        int nYear;
        int nMonth;
        int nDay;
        int nHour;
        int nMin;
        DateTime MyDateTime;
        EItems currItem = EItems.E_MAT;

        public Form1()
        {
            InitializeComponent();

            nCroodX = rand.Next(50, 430);
            nCroodY= rand.Next(50, 750);


            MyDateTime = DateTime.Now;
            nYear = MyDateTime.Year;
            nMonth = MyDateTime.Month;
            nDay = MyDateTime.Day;
            nHour = MyDateTime.Hour + 1;
            nMin = MyDateTime.Minute;

            txtCroodX.Text = nCroodX.ToString();
            txtCroodY.Text = nCroodY.ToString();

            cboHour.SelectedIndex = nHour - 2;
            txtMinute.Text = nMin.ToString();
            txtMinute.Maximum = 59;
            txtMinute.Minimum = 00;

            cboMaterial.SelectedIndex = (int)currItem;
            

            barColor = new Color[12];
            barColor[0] = Color.FromArgb(255, 200, 255, 255);
            barColor[1] = Color.FromArgb(255, 150, 200, 255);
            barColor[2] = Color.FromArgb(255, 100, 100, 200);
            barColor[3] = Color.FromArgb(255, 255, 60, 130);
            barColor[4] = Color.FromArgb(255, 250, 200, 255);
            barColor[5] = Color.FromArgb(255, 255, 255, 0);
            barColor[6] = Color.FromArgb(255, 255, 155, 55);
            barColor[7] = Color.FromArgb(255, 150, 200, 155);
            barColor[8] = Color.FromArgb(255, 255, 255, 200);
            barColor[9] = Color.FromArgb(255, 100, 150, 200);
            barColor[10] = Color.FromArgb(255, 130, 235, 250);
            barColor[11] = Color.FromArgb(255, 150, 240, 80);
            //for (int i = 0; i < barColor.Length; ++i)
            //{
            //    barColor[i] = Color.FromArgb(255, 10, 10, 10);
            //}


            // Create, no need if you added the chart by visual editor
            barChart = new HBarChart();
            this.panel1.Controls.Add(barChart);
            barChart.Dock = DockStyle.Fill;

            //barChart.Background.
            //barChart.SizingMode = HBarChart.BarSizingMode.AutoScale;
            barChart.Border.Width = 10;
            barChart.Shadow.Mode = CShadowProperty.Modes.Outer;
            barChart.Shadow.WidthInner = 1;
            barChart.Shadow.WidthOuter = 4;
            barChart.Shadow.ColorOuter = Color.FromArgb(100, 0, 0, 0);


            //// Register for control events
            //barChart.BarClicked += new HBarChart.OnBarEvent(barChart_BarClicked);
            //barChart.BarDoubleClicked += new HBarChart.OnBarEvent(barChart_BarDoubleClicked);
            //barChart.BarMouseEnter += new HBarChart.OnBarEvent(barChart_BarMouseEnter);
            //barChart.BarMouseLeave += new HBarChart.OnBarEvent(barChart_BarMouseLeave);

            UpdateChart();
        }

        private void UpdateChart()
        {
            if (barChart == null)
                return;

            for (int i = 0; i < arrRes.Count; ++i)
                barChart.RemoveAt(0);

            //barChart.RedrawChart();
            arrRes.Clear();
            arrNextHourPrices.Clear();
            arrFinalPrices.Clear();
            arrInventory.Clear();
            
            for (int i = 0; i < P_COUNT; ++i)   // Palnet count
            {
                //float fPosConst = GetPositionConst(nCroodX, nCroodY, i);
                //float fTimeConst = GetTimeConst();

                float fConst = GetPositionTimeConst(nCroodX, nCroodY, i, (int)(currItem+1));
                float fNextConst = GetNextTimeConst(nCroodX, nCroodY, i, (int)(currItem+1));

                arrRes.Add(fConst);
                arrNextHourPrices.Add(fNextConst);

                float fTemp = ((fNextConst - fConst) * ((float)nMin / 60.0f));   // 다음Hour와 현재Hour의 차이만큼 interpolation함.
                float fFinalConst = fConst + fTemp;
                arrFinalPrices.Add(fFinalConst);

                lstLog.Items.Add("("+nCroodX+","+nCroodY+"):"+i+" --> "+fFinalConst);
                lstLog.SelectedIndex = lstLog.Items.Count - 1;

                arrInventory.Add(new BaseItem(currItem, fFinalConst));
            }

            for (int i = 0; i < arrInventory.Count; ++i)
            {
                float fValue = (float)((BaseItem)arrInventory[i]).nCurrentPrice;
                barChart.Add(fValue, (i + 1) + " Planet", barColor[i]);
            }
            barChart.RedrawChart();
        }



        private float GetPositionConst(int x, int y, int p)
        {
            float fRes = ((x + 1) * (y + 1) * (p + 2) / (((x + 1) % (p + 2)) + 1)) / 100000.0f;

            int nMultiCalcCount = 0, nLogCalcCount = 0;
            while (fRes < 0.09f)	// 0.05보다 작으면
            {
                fRes *= (p + 2);
                ++nMultiCalcCount;
            }

            while (fRes > 1.0f)	// 1이 넘어가면
            {
                fRes = (float)Math.Log(fRes);
                ++nLogCalcCount;
            }

            //fRes *= 200;

            return fRes;
        }

        private float GetNextTimeConst(int x, int y, int p, int i)
        {
            int nNextHour = nHour + 1;
            float fParam1 = (nNextHour % nYear * i) * (nDay*i + 10 % nNextHour) + nMonth;
            float fParam2 = (((x % nNextHour) + (y % nNextHour)) / (i*3 + p + 2)) + i;

            float fRes = fParam1 / fParam2;
            fRes /= 100000.0f;
            fRes = Math.Abs(fRes);

            int nMultiCalcCount = 0, nLogCalcCount = 0;
            while (fRes < 0.09f)	// 0.05보다 작으면
            {
                fRes *= (p + 2);
                ++nMultiCalcCount;
            }

            while (fRes > 1.0f)	// 1이 넘어가면
            {
                fRes = (float)Math.Log(fRes);
                ++nLogCalcCount;
            }

            return fRes;
        }

        private float GetPositionTimeConst(int x, int y, int p, int i)
        {
            float fParam1 = (nHour % nYear) * (nDay*i + 10 % nHour) + nMonth;
            float fParam2 = (((x % nHour) + (y % nHour)) / (i *3 + p + 2)) + i;
            
           

            float fRes= fParam1 / fParam2;
            fRes /= 100000.0f;
            fRes = Math.Abs(fRes);

            //lstLog.Items.Add(fParam1 + " / " + fParam2 + " = " + fRes);
            //lstLog.SelectedIndex = lstLog.Items.Count - 1;

            //txtAlgorithm.Text = fParam1 + " / " + fParam2 + " = " + fRes;

            int nMultiCalcCount = 0, nLogCalcCount = 0;
            while (fRes < 0.09f)	// 0.05보다 작으면
            {
                fRes *= (p + 2);
                ++nMultiCalcCount;
            }

            while (fRes > 1.0f)	// 1이 넘어가면
            {
                fRes = (float)Math.Log(fRes);
                ++nLogCalcCount;
            }
            return fRes;
        }

        private float GetTimeConst()
        {
            int nAlgorithm = (int)((nYear + (nMonth * 12)) % (nDay * nHour));
            // 이 알고리즘 (최대:719, 최소:0, 평균:95)

            float fTimeConst = ((float)nAlgorithm / 719f);
            return fTimeConst;
        }

        private void txtCroodX_TextChanged(object sender, EventArgs e)
        {
            if (txtCroodX.Text.Equals(""))
                txtCroodX.Text = "0";

            nCroodX= Int32.Parse(txtCroodX.Text);
            this.UpdateChart();
        }
        private void txtCroodY_TextChanged(object sender, EventArgs e)
        {
            if (txtCroodY.Text.Equals(""))
                txtCroodY.Text = "0";

            nCroodY = Int32.Parse(txtCroodY.Text);
            this.UpdateChart();
        }

        private void txtCroodX_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (!(char.IsDigit(e.KeyChar) || e.KeyChar == Convert.ToChar(Keys.Back)))
            {
                e.Handled = true;
            }
        }

        private void txtCroodY_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (!(char.IsDigit(e.KeyChar) || e.KeyChar == Convert.ToChar(Keys.Back)))
            {
                e.Handled = true;
            }
        }

        private void cboHour_SelectedIndexChanged(object sender, EventArgs e)
        {
            nHour = cboHour.SelectedIndex + 2;
            //this.UpdateChart();
        }

        //private void txtMinute_TextChanged(object sender, EventArgs e)
        //{
        //    int nMin= Int32.Parse(txtMinute.Text);
        //    if (nMin >= 60)
        //    {
        //        txtMinute.Text = "59";
        //    }
        //    this.UpdateChart();
        //}

        private void dateTimePicker1_ValueChanged(object sender, EventArgs e)
        {
            MyDateTime= DatePicker.Value;
            nYear = MyDateTime.Year;
            nMonth = MyDateTime.Month;
            nDay = MyDateTime.Day;
            nHour = MyDateTime.Hour + 1;
            nMin = MyDateTime.Minute;
            //this.UpdateChart();
        }

        private void txtMinute_ValueChanged(object sender, EventArgs e)
        {
            nMin = Int32.Parse(txtMinute.Text);
            
            //if (nMin + txtMinute.Maximum >= 60)
            //{
            //    txtMinute.Text = "59";
            //}
            //this.UpdateChart();
        }

        private void timer_auto_Tick(object sender, EventArgs e)
        {
            int nCheckMin = Int32.Parse(txtMinute.Text);
            if (nCheckMin + txtMinute.Increment == 60)
            {
                if (cboHour.Items.Count-1 == cboHour.SelectedIndex) // 타임 끝나면 [3/19/2013 ChoiJunHyeok]
                    timer_auto.Enabled = false;
                else
                    cboHour.SelectedIndex += 1;

                txtMinute.Text = "0";
                nMin = 0;
            }
            else
            {
                txtMinute.UpButton();
            }

            this.UpdateChart();
            
        }

        private void chkAuto_CheckedChanged(object sender, EventArgs e)
        {
            if (chkAuto.Checked)
            {
                cboMaterial.Enabled = false;
                timer_auto.Enabled = true;
            }
            else
            {
                cboMaterial.Enabled = true;
                timer_auto.Enabled = false;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.UpdateChart();
        }

        private void cboMaterial_SelectedIndexChanged(object sender, EventArgs e)
        {
            //cboMaterial.SelectedIndex = (int)currItem;
            switch (cboMaterial.SelectedIndex)
            {
                case 0:
                    currItem = EItems.E_BOX;
                    break;
                case 1:
                    currItem = EItems.E_MAT;
                    break;
                case 2:
                    currItem = EItems.E_GAS;
                    break;
            }
            this.UpdateChart();
        }
      
    }
}