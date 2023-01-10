
PACKAGECONFIG_remove_class-target = "python"

# The attempt to conditionally inherit a python related class in the libxml2
# recipe based on PACKAGECONFIG doesn't work as expected (the class is always
# inherited even when python is being removed from PACKAGECONFIG). See:
# https://lore.kernel.org/all/aebe57d858d895a9266a9a25e90b60c8a34dd9e3.camel@redrectangle.org/T/
# As a workaround, try to undo the effects of the inherited class...

EXTRA_PYTHON_DEPENDS_remove_class-target = "python3"
