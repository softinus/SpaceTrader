namespace WindowsApplication1
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.panel1 = new System.Windows.Forms.Panel();
            this.DatePicker = new System.Windows.Forms.DateTimePicker();
            this.cboMaterial = new System.Windows.Forms.ComboBox();
            this.cboHour = new System.Windows.Forms.ComboBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.txtCroodX = new System.Windows.Forms.TextBox();
            this.txtCroodY = new System.Windows.Forms.TextBox();
            this.txtPlanetNum = new System.Windows.Forms.TextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.txtMinute = new System.Windows.Forms.NumericUpDown();
            this.lstLog = new System.Windows.Forms.ListBox();
            this.chkAuto = new System.Windows.Forms.CheckBox();
            this.timer_auto = new System.Windows.Forms.Timer(this.components);
            this.button1 = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.txtMinute)).BeginInit();
            this.SuspendLayout();
            // 
            // panel1
            // 
            this.panel1.Location = new System.Drawing.Point(3, 75);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(562, 503);
            this.panel1.TabIndex = 0;
            // 
            // DatePicker
            // 
            this.DatePicker.Location = new System.Drawing.Point(50, 12);
            this.DatePicker.Name = "DatePicker";
            this.DatePicker.Size = new System.Drawing.Size(200, 21);
            this.DatePicker.TabIndex = 1;
            this.DatePicker.ValueChanged += new System.EventHandler(this.dateTimePicker1_ValueChanged);
            // 
            // cboMaterial
            // 
            this.cboMaterial.FormattingEnabled = true;
            this.cboMaterial.Items.AddRange(new object[] {
            "(Lv1)Box",
            "(Lv2)Material",
            "(Lv3)Gas"});
            this.cboMaterial.Location = new System.Drawing.Point(694, 12);
            this.cboMaterial.Name = "cboMaterial";
            this.cboMaterial.Size = new System.Drawing.Size(121, 20);
            this.cboMaterial.TabIndex = 2;
            this.cboMaterial.SelectedIndexChanged += new System.EventHandler(this.cboMaterial_SelectedIndexChanged);
            // 
            // cboHour
            // 
            this.cboHour.FormattingEnabled = true;
            this.cboHour.Items.AddRange(new object[] {
            "1시",
            "2시",
            "3시",
            "4시",
            "5시",
            "6시",
            "7시",
            "8시",
            "9시",
            "10시",
            "11시",
            "12시",
            "13시",
            "14시",
            "15시",
            "16시",
            "17시",
            "18시",
            "19시",
            "20시",
            "21시",
            "22시",
            "23시",
            "24시"});
            this.cboHour.Location = new System.Drawing.Point(257, 12);
            this.cboHour.Name = "cboHour";
            this.cboHour.RightToLeft = System.Windows.Forms.RightToLeft.Yes;
            this.cboHour.Size = new System.Drawing.Size(56, 20);
            this.cboHour.TabIndex = 3;
            this.cboHour.SelectedIndexChanged += new System.EventHandler(this.cboHour_SelectedIndexChanged);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(6, 15);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(41, 12);
            this.label1.TabIndex = 5;
            this.label1.Text = "시간 : ";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(6, 46);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(41, 12);
            this.label2.TabIndex = 6;
            this.label2.Text = "위치 : ";
            // 
            // txtCroodX
            // 
            this.txtCroodX.Location = new System.Drawing.Point(50, 41);
            this.txtCroodX.MaxLength = 3;
            this.txtCroodX.Name = "txtCroodX";
            this.txtCroodX.Size = new System.Drawing.Size(100, 21);
            this.txtCroodX.TabIndex = 7;
            this.txtCroodX.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.txtCroodX.TextChanged += new System.EventHandler(this.txtCroodX_TextChanged);
            this.txtCroodX.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.txtCroodX_KeyPress);
            // 
            // txtCroodY
            // 
            this.txtCroodY.Location = new System.Drawing.Point(156, 41);
            this.txtCroodY.MaxLength = 3;
            this.txtCroodY.Name = "txtCroodY";
            this.txtCroodY.Size = new System.Drawing.Size(100, 21);
            this.txtCroodY.TabIndex = 8;
            this.txtCroodY.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.txtCroodY.TextChanged += new System.EventHandler(this.txtCroodY_TextChanged);
            this.txtCroodY.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.txtCroodY_KeyPress);
            // 
            // txtPlanetNum
            // 
            this.txtPlanetNum.Enabled = false;
            this.txtPlanetNum.Location = new System.Drawing.Point(278, 41);
            this.txtPlanetNum.MaxLength = 2;
            this.txtPlanetNum.Name = "txtPlanetNum";
            this.txtPlanetNum.Size = new System.Drawing.Size(25, 21);
            this.txtPlanetNum.TabIndex = 9;
            this.txtPlanetNum.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(263, 47);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(9, 12);
            this.label3.TabIndex = 10;
            this.label3.Text = ":";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(368, 16);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(17, 12);
            this.label4.TabIndex = 12;
            this.label4.Text = "분";
            // 
            // txtMinute
            // 
            this.txtMinute.Location = new System.Drawing.Point(319, 13);
            this.txtMinute.Name = "txtMinute";
            this.txtMinute.Size = new System.Drawing.Size(43, 21);
            this.txtMinute.TabIndex = 13;
            this.txtMinute.ValueChanged += new System.EventHandler(this.txtMinute_ValueChanged);
            // 
            // lstLog
            // 
            this.lstLog.FormattingEnabled = true;
            this.lstLog.ItemHeight = 12;
            this.lstLog.Location = new System.Drawing.Point(571, 75);
            this.lstLog.Name = "lstLog";
            this.lstLog.Size = new System.Drawing.Size(244, 496);
            this.lstLog.TabIndex = 14;
            // 
            // chkAuto
            // 
            this.chkAuto.AutoSize = true;
            this.chkAuto.Location = new System.Drawing.Point(319, 45);
            this.chkAuto.Name = "chkAuto";
            this.chkAuto.Size = new System.Drawing.Size(69, 16);
            this.chkAuto.TabIndex = 15;
            this.chkAuto.Text = "Auto IC.";
            this.chkAuto.UseVisualStyleBackColor = true;
            this.chkAuto.CheckedChanged += new System.EventHandler(this.chkAuto_CheckedChanged);
            // 
            // timer_auto
            // 
            this.timer_auto.Tick += new System.EventHandler(this.timer_auto_Tick);
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(400, 12);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 23);
            this.button1.TabIndex = 16;
            this.button1.Text = "&Refresh";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(819, 578);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.chkAuto);
            this.Controls.Add(this.lstLog);
            this.Controls.Add(this.txtMinute);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.txtPlanetNum);
            this.Controls.Add(this.txtCroodY);
            this.Controls.Add(this.txtCroodX);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.cboHour);
            this.Controls.Add(this.cboMaterial);
            this.Controls.Add(this.DatePicker);
            this.Controls.Add(this.panel1);
            this.DoubleBuffered = true;
            this.Name = "Form1";
            this.Text = "Trader";
            ((System.ComponentModel.ISupportInitialize)(this.txtMinute)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.DateTimePicker DatePicker;
        private System.Windows.Forms.ComboBox cboMaterial;
        private System.Windows.Forms.ComboBox cboHour;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TextBox txtCroodX;
        private System.Windows.Forms.TextBox txtCroodY;
        private System.Windows.Forms.TextBox txtPlanetNum;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.NumericUpDown txtMinute;
        private System.Windows.Forms.ListBox lstLog;
        private System.Windows.Forms.CheckBox chkAuto;
        private System.Windows.Forms.Timer timer_auto;
        private System.Windows.Forms.Button button1;
    }
}

