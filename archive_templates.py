import shutil
from pathlib import Path

supported_languages = ["java", "python"]
current_working_directory = Path("./").resolve()
output_dir = Path.home().joinpath("archive-templates").resolve()
Path.mkdir(output_dir, parents=True, exist_ok=True)

for language in supported_languages:
    templates_dir = Path(current_working_directory).joinpath("templates").joinpath(language).resolve()
    templates_sub_dir = [file for file in templates_dir.iterdir() if file.is_dir()]
    for template in templates_sub_dir:
        zip_file_name = template.name + "-" + language
        zip_file_path = Path(output_dir).joinpath(zip_file_name).resolve()
        shutil.make_archive(zip_file_name, "zip", root_dir=template)
        print("Created zip file for template " + zip_file_name)
