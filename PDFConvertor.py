from fpdf import FPDF
from datetime import datetime

def create_pdf_with_github_link(text, github_url, pdf_name):
    #Create PDF object
    pdf = FPDF()

    # Add a page
    pdf.add_page()
    #Set Font
    pdf.set_font("Arial", size=12)
    try:
    # Add some text
        pdf.cell(0, 10, "Test PDF Creation", ln=True)
        pdf.cell(0, 10, f"Created on: {datetime.now()}", ln=True)
        pdf.image("github.png", x=10, y=pdf.get_y(), w=10)
    except Exception as e:
        print(f"Error creating PDF: {e}")
    #Add github link
    pdf.set_text_color(0,0,255)
    # Create a clickable link
    pdf.cell(0,10,txt=github_url,link=github_url)

    #Save the PDF
    pdf.output(pdf_name)

#Call the function
if __name__ == "__main__":
    print("Enter your text(Press Enter to finish): ")
    lines = []
    while True:
        line = input()
        if line == "":
            lines.append(line)
            break
    
    text = "\n".join(lines)
    #Get Github repository URL
    github = input("Enter your Github URL: ")

    #Create PDF
    create_pdf_with_github_link(text, github, "Github_Link.pdf")
    print("PDF Created Successfully")