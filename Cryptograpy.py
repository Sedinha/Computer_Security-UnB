import os
import textwrap

def clear_screen():
    os.system('cls' if os.name == 'nt' else 'clear')

def center_text(text, width):
    return text.center(width)

def print_welcome_message():
    terminal_width = os.get_terminal_size().columns
    welcome_message = "WELCOME TO MY ETHICAL HACKER COURSE"
    centered_welcome_message = center_text(welcome_message, terminal_width)
    
    print("\n" * 3)  # Add some vertical space
    print(centered_welcome_message)
    print("\n" * 2)  # Add some vertical space

def print_menu():
    terminal_width = os.get_terminal_size().columns
    menu = ["Exercise 1", "Exercise 2"]
    
    for item in menu:
        item_width = len(item) + 3  # Adding padding for the box
        padding = (terminal_width - item_width) // 2
        horizontal_border = '-' * item_width
        
        print(' ' * padding + '+' + horizontal_border + '+')
        print(' ' * padding + '| ' + item + ' |')
        print(' ' * padding + '+' + horizontal_border + '+')
        
        if item != menu[-1]:
            print()
    
def main():
    clear_screen()
    print_welcome_message()
    print_menu()
    
    while True:
        try:
            choice = input("\nEnter the number of the exercise you want to start (1 or 2): ")
            if choice == '1':
                print("\nStarting Exercise 1...")
                # Placeholder for Exercise 1 function call
                break
            elif choice == '2':
                print("\nStarting Exercise 2...")
                # Placeholder for Exercise 2 function call
                break
            else:
                print("Invalid input. Please enter 1 or 2.")
        except KeyboardInterrupt:
            print("\nExiting the program.")
            break

if __name__ == "__main__":
    main()