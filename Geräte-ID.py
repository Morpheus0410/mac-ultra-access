from jnius import autoclass
import tkinter as tk

# =========================
# Farben / Style
# =========================
BG = "#0b1220"
CARD = "#111827"
TEXT = "#f3f4f6"
MUTED = "#9ca3af"
GREEN = "#22c55e"
ENTRY_BG = "#1e293b"

# =========================
# Android-ID holen
# =========================
def get_android_id():
    try:
        SettingsSecure = autoclass("android.provider.Settings$Secure")
        PythonActivity = autoclass("org.kivy.android.PythonActivity")

        activity = PythonActivity.mActivity

        android_id = SettingsSecure.getString(
            activity.getContentResolver(),
            SettingsSecure.ANDROID_ID
        )

        return str(android_id)

    except Exception as e:
        return f"FEHLER: {e}"

device_id = get_android_id()

# =========================
# Fenster
# =========================
root = tk.Tk()
root.title("Android-ID")
root.geometry("420x230")
root.configure(bg=BG)
root.resizable(False, False)

# =========================
# Card
# =========================
card = tk.Frame(
    root,
    bg=CARD,
    padx=18,
    pady=18
)

card.pack(
    padx=15,
    pady=15,
    fill="both",
    expand=True
)

# =========================
# Titel
# =========================
title = tk.Label(
    card,
    text="Android-ID",
    font=("Arial", 20, "bold"),
    bg=CARD,
    fg=TEXT
)

title.pack(pady=(0, 5))

# =========================
# Untertitel
# =========================
subtitle = tk.Label(
    card,
    text="",
    font=("Arial", 10),
    bg=CARD,
    fg=MUTED
)

subtitle.pack(pady=(0, 15))

# =========================
# ID Feld
# =========================
entry = tk.Entry(
    card,
    font=("Consolas", 14),
    justify="center",
    bg=ENTRY_BG,
    fg=TEXT,
    insertbackground=TEXT,
    relief="flat"
)

entry.pack(
    fill="x",
    ipady=10
)

entry.insert(0, device_id)

# =========================
# Kopieren
# =========================
def copy_id():

    root.clipboard_clear()
    root.clipboard_append(device_id)
    root.update()

    status.config(
        text="Android-ID kopiert"
    )

# =========================
# Button
# =========================
button = tk.Button(
    card,
    text="ID KOPIEREN",
    font=("Arial", 12, "bold"),
    bg=GREEN,
    fg="black",
    activebackground="#16a34a",
    relief="flat",
    pady=10,
    command=copy_id
)

button.pack(
    fill="x",
    pady=15
)

# =========================
# Status
# =========================
status = tk.Label(
    card,
    text="Bereit",
    font=("Arial", 10),
    bg=CARD,
    fg=MUTED
)

status.pack()

# =========================
# Start
# =========================
root.mainloop()