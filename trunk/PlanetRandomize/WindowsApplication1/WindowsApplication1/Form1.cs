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

        int nCroodX = 341;
        int nCroodY = 754;
        ArrayList arrRes = new ArrayList();
        int nYear;
        int nMonth;
        int nDay;
        int nHour;
        DateTime MyDateTime;

        public Form1()
        {
            InitializeComponent();

            MyDateTime = DateTime.Now;
            nYear = MyDateTime.Year;
            nMonth = MyDateTime.Month;
            nDay = MyDateTime.Day;
            nHour = MyDateTime.Hour + 1;

            txtCroodX.Text = nCroodX.ToString();
            txtCroodY.Text = nCroodY.ToString();

            cboHour.SelectedIndex = nHour - 2;
            txtMinute.Text = MyDateTime.Minute.ToString();
            

            barColor = new Color[12];
            barColor[0]= Color.FromArgb(255, 200, 255, 255);
            barColor[1]= Color.FromArgb(255, 150, 200, 255);
            barColor[2]= Color.FromArgb(255, 100, 100, 200);
            barColor[3]= Color.FromArgb(255, 255, 60, 130);
            barColor[4]= Color.FromArgb(255, 250, 200, 255);
            barColor[5]= Color.FromArgb(255, 255, 255, 0);
            barColor[6]= Color.FromArgb(255, 255, 155, 55);
            barColor[7]= Color.FromArgb(255, 150, 200, 155);
            barColor[8]= Color.FromArgb(255, 255, 255, 200);
            barColor[9]= Color.FromArgb(255, 100, 150, 200);
            barColor[10]=Color.FromArgb(255, 130, 235, 250);
            barColor[11]=Color.FromArgb(255, 150, 240, 80);


            // Create, no need if you added the chart by visual editor
            barChart = new HBarChart();
            this.panel1.Controls.Add(barChart);
            barChart.Dock = DockStyle.Fill;


            barChart.Border.Width = 10;
            barChart.Shadow.Mode = CShadowProperty.Modes.Both;
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

            barChart.RedrawChart();
            arrRes.Clear();

            
            for (int i = 0; i < P_COUNT; ++i)
            {
                float fPosConst = GetPositionConst(nCroodX, nCroodY, i);
                float fTimeConst = GetTimeConst();

                float fConst = GetPositionTimeConst(nCroodX, nCroodY, i);

                arrRes.Add(fPosConst);
            }

            for (int i = 0; i < arrRes.Count; ++i)
            {
                float fValue = (float)arrRes[i];
                barChart.Add(fValue, (i + 1) + "번 행성", barColor[i]);
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

        private float GetPositionTimeConst(int x, int y, int p)
        {
            float fRes = ((x + 1) * (y + 1) * (p + 2) * (nYear + (nMonth * 12) / (((x + 1) % (p + 2)) + (nDay * nHour) + 1)));
            fRes /= 1000000000.0f;
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
            this.UpdateChart();
        }

        private void txtMinute_TextChanged(object sender, EventArgs e)
        {
            int nMin= Int32.Parse(txtMinute.Text);
            if (nMin >= 60)
            {
                txtMinute.Text = "59";
            }
            this.UpdateChart();
        }

        private void dateTimePicker1_ValueChanged(object sender, EventArgs e)
        {
            MyDateTime= DatePicker.Value;
            nYear = MyDateTime.Year;
            nMonth = MyDateTime.Month;
            nDay = MyDateTime.Day;
            nHour = MyDateTime.Hour + 1;
            this.UpdateChart();
        }

      
    }
}